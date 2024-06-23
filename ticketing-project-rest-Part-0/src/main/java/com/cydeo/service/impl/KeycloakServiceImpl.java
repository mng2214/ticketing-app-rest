package com.cydeo.service.impl;

import com.cydeo.config.KeycloakProperties;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.KeycloakService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import javax.ws.rs.core.Response;
import java.util.List;

import static java.util.Arrays.asList;
import static org.keycloak.admin.client.CreatedResponseUtil.getCreatedId;

@Service
public class KeycloakServiceImpl implements KeycloakService {


    private final KeycloakProperties keycloakProperties;

    public KeycloakServiceImpl(KeycloakProperties keycloakProperties) {
        this.keycloakProperties = keycloakProperties;
    }

//    @Override
//    public Response userCreate(UserDTO userDTO) {
//
//        CredentialRepresentation credential = new CredentialRepresentation();
//        credential.setType(CredentialRepresentation.PASSWORD);
//        credential.setTemporary(false);
//        credential.setValue(userDTO.getPassWord());
//
//        UserRepresentation keycloakUser = new UserRepresentation();
//        keycloakUser.setUsername(userDTO.getUserName());
//        keycloakUser.setFirstName(userDTO.getFirstName());
//        keycloakUser.setLastName(userDTO.getLastName());
//        keycloakUser.setEmail(userDTO.getUserName());
//        keycloakUser.setCredentials(asList(credential));
//        keycloakUser.setEmailVerified(true);
//        keycloakUser.setEnabled(true);
//
//        Keycloak keycloak = getKeycloakInstance();
//
//        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
//        UsersResource usersResource = realmResource.users();
//
//        // Create Keycloak user
//        Response result = usersResource.create(keycloakUser);
//
//        String userId = getCreatedId(result);
//        ClientRepresentation appClient = realmResource.clients()
//                .findByClientId(keycloakProperties.getClientId()).get(0);
//
//        RoleRepresentation userClientRole = realmResource.clients().get(appClient.getId()) //
//                .roles().get(userDTO.getRole().getDescription()).toRepresentation();
//
//        realmResource.users().get(userId).roles().clientLevel(appClient.getId())
//                .add(List.of(userClientRole));
//
//
//        keycloak.close();
//        return result;
//    }
//
//
//    @Override
//    public void delete(String userName) {
//
//        Keycloak keycloak = getKeycloakInstance();
//
//        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
//        UsersResource usersResource = realmResource.users();
//
//        List<UserRepresentation> userRepresentations = usersResource.search(userName);
//        String uid = userRepresentations.get(0).getId();
//        usersResource.delete(uid);
//
//        keycloak.close();
//    }
//
//    private Keycloak getKeycloakInstance() {
//        return Keycloak.getInstance(keycloakProperties.getAuthServerUrl(),
//                keycloakProperties.getMasterRealm(), keycloakProperties.getMasterUser(),
//                keycloakProperties.getMasterUserPswd(), keycloakProperties.getMasterClient());
//    }
@Override
public Response userCreate(UserDTO userDTO) {
    Keycloak keycloak = null;
    try {
        // Create CredentialRepresentation
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setTemporary(false);
        credential.setValue(userDTO.getPassWord());

        // Create UserRepresentation
        UserRepresentation keycloakUser = new UserRepresentation();
        keycloakUser.setUsername(userDTO.getUserName());
        keycloakUser.setFirstName(userDTO.getFirstName());
        keycloakUser.setLastName(userDTO.getLastName());
        keycloakUser.setEmail(userDTO.getUserName());
        keycloakUser.setCredentials(asList(credential));
        keycloakUser.setEmailVerified(true);
        keycloakUser.setEnabled(true);

        // Log the user representation details
        System.out.println("Creating Keycloak user with the following details:");
        System.out.println("Username: " + keycloakUser.getUsername());
        System.out.println("FirstName: " + keycloakUser.getFirstName());
        System.out.println("LastName: " + keycloakUser.getLastName());
        System.out.println("Email: " + keycloakUser.getEmail());
        System.out.println("Credentials: " + keycloakUser.getCredentials());
        System.out.println("EmailVerified: " + keycloakUser.isEmailVerified());
        System.out.println("Enabled: " + keycloakUser.isEnabled());

        keycloak = getKeycloakInstance();
        RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
        UsersResource usersResource = realmResource.users();

        // Create Keycloak user
        Response result = usersResource.create(keycloakUser);

        if (result.getStatus() != 201) {
            // Log error details
            System.err.println("Failed to create user. Response: " + result.getStatus() + " " + result.getStatusInfo());
            System.err.println("Error message: " + result.readEntity(String.class));
            return result;
        }

        String userId = getCreatedId(result);
        ClientRepresentation appClient = realmResource.clients()
                .findByClientId(keycloakProperties.getClientId()).get(0);

        RoleRepresentation userClientRole = realmResource.clients().get(appClient.getId())
                .roles().get(userDTO.getRole().getDescription()).toRepresentation();

        realmResource.users().get(userId).roles().clientLevel(appClient.getId())
                .add(List.of(userClientRole));

        return result;
    } catch (Exception e) {
        // Log exception details
        e.printStackTrace();
        System.err.println("An error occurred while creating the user in Keycloak: " + e.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    } finally {
        if (keycloak != null) {
            keycloak.close();
        }
    }
}

    @Override
    public void delete(String userName) {
        Keycloak keycloak = null;
        try {
            keycloak = getKeycloakInstance();
            RealmResource realmResource = keycloak.realm(keycloakProperties.getRealm());
            UsersResource usersResource = realmResource.users();

            List<UserRepresentation> userRepresentations = usersResource.search(userName);
            String uid = userRepresentations.get(0).getId();
            usersResource.delete(uid);
        } catch (Exception e) {
            // Log exception details
            e.printStackTrace();
            System.err.println("An error occurred while deleting the user in Keycloak: " + e.getMessage());
        } finally {
            if (keycloak != null) {
                keycloak.close();
            }
        }
    }

    private Keycloak getKeycloakInstance() {
        return Keycloak.getInstance(keycloakProperties.getAuthServerUrl(),
                keycloakProperties.getMasterRealm(), keycloakProperties.getMasterUser(),
                keycloakProperties.getMasterUserPswd(), keycloakProperties.getMasterClient());
    }

}
