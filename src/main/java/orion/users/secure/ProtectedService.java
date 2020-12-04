/**
 * @License
 * Copyright 2020 Orion Services
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package orion.users.secure;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import orion.users.data.UserDAO;
import orion.users.model.User;

@Path("/api/v1/")
@RequestScoped

public class ProtectedService {

    @Inject
    private UserDAO userDAO;

    @Inject
    private JsonWebToken jwt;


    /**
     * Retrieves a user from the database
     * 
     * @param id The user's id
     * @return An user object
     */
    @GET
    @APIResponse(responseCode ="200", description ="successfully")
    @APIResponse(responseCode ="409", description ="a conflict has occurred")
    @Tag(name="CRUD")
    @Path("/readProtected/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"users"})
    @Transactional
    public User readProtected(@PathParam("id") final long id) {
        return userDAO.find(id);
    }

    /**
     * Deletes an user from the database
     * 
     * @param id The user's id
     */
    @POST
    @APIResponse(responseCode ="200", description ="successfully")
    @APIResponse(responseCode ="409", description ="a conflict has occurred")
    @Tag(name="CRUD")
    @Path("/deleteProtected")
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"users"})
    @Transactional
    public User deleteProtected(@FormParam("id") final long id) {
        // find the id and delete the user data
        
        final User usr = userDAO.find(id);
        userDAO.delete(usr);
        return usr;
    }

    /**
     * Updates an user in database
     * 
     * @param id       The user's id
     * @param name     The user's name
     * @param email    The user's e-mail
     * @param password The user's password
     * @return
     */
    @POST
    @APIResponse(responseCode ="200", description ="successfully")
    @APIResponse(responseCode ="409", description ="a conflict has occurred")
    @Tag(name="CRUD")
    @Path("/updateProtected")
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"users"})
    @Transactional
    public User updateProtected(@FormParam("id") final long id, @FormParam("name") final String name,
            @FormParam("email") final String email, @FormParam("password") final String password) {

        final User usr = userDAO.find(id);
        usr.setName(name);
        usr.setEmail(email);
        usr.setPassword(password);
        userDAO.update(usr);
        return usr;
    }
}