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
package orion.users.service;


import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ibm.websphere.security.jwt.Claims;
import com.ibm.websphere.security.jwt.InvalidBuilderException;
import com.ibm.websphere.security.jwt.InvalidClaimException;
import com.ibm.websphere.security.jwt.JwtBuilder;
import com.ibm.websphere.security.jwt.JwtException;

import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import orion.users.data.UserDAO;
import orion.users.model.User;
import orion.users.util.JavaMailUtil;
import orion.users.util.ValidateEmail;

@RequestScoped
@Path("/api/v1/")
public class PublicService {

    @Inject
    private UserDAO userDAO;

    /**
     * Creates a new user in the database
     * 
     * @param name     The name of a user
     * @param email    The email of a user
     * @param password The password of a user
     * 
     * @return An user object
     */
    @POST
    @APIResponse(responseCode = "200", description = "successfully")
    @APIResponse(responseCode = "409", description = "a conflict has occurred")
    @Tag(name="CRUD")
    @Path("create")
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public User createUser(@FormParam("name") final String name, @FormParam("email") final String email,
            @FormParam("password") final String password) throws WebApplicationException, NotFoundException, Exception {

        final User usr = new User();

        // in checkMail method, email is checked, if it is equal to one that already
        // exists in the bank,
        // it does not create
        if (checkMail(email).equals(false)) {

            if(name.isEmpty() || email.isEmpty() || password.isEmpty()){
                String message = "No fields can be left empty";
                throw new NotFoundException(message);
            }
               
              else if( ValidateEmail.validateMail(email) == false){
                String message = "Invalid email";
                throw new NotFoundException(message);
              }

              else {
                String hashcode = usr.setHash(userDAO.generateHash());
                String link = "http://localhost:9080/orion-users-service/isvalid?hash=";
                JavaMailUtil.sendMail(email, hashcode, link);

                usr.setName(name);
                usr.setEmail(email);
                usr.setPassword(password);
                userDAO.create(usr);
              }
                  

                return usr;

        } else {
                String message = "The informed e-mail already exist in the service";
                throw new WebApplicationException(message, Response.Status.CONFLICT);
        }
    }

    /**
     * Method created exclusively to be used in integration tests, in situations
     * where you need to change the Verified attribute to TRUE state
     * @param email
     * @return
     */

    @POST
    @APIResponse(responseCode = "200", description = "successfully")
    @APIResponse(responseCode = "409", description = "a conflict has occurred")
    @Tag(name="AUTH")
    @Path("autoConfirm")
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public User autoConfirmForTest(@FormParam("email") final String email) {

        final User usr = userDAO.find("email", email);
            usr.setVerified(true);
            userDAO.update(usr);
            usr.setHash(null);
        return usr;
    }

    

    @POST
    @APIResponse(responseCode = "200", description = "successfully")
    @APIResponse(responseCode = "409", description = "a conflict has occurred")
    @Tag(name="AUTH")
    @Path("confirmHash")
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String confirmHash(@FormParam("hash") final String hash)
            throws Exception {

        String mail;

        try {
            // check if there is a hash in the database
            final User usr = userDAO.find("hash", hash);
            // send email to user

            // if atribute users's hash is false, cancel change
            usr.getHash().equals(hash);

            usr.setVerified(true);
            userDAO.update(usr);
            mail = "complete!";
            usr.setHash(null);

        } catch (NoResultException e) {
            mail = "failed, try again";
        }
        return mail;
    }

    @POST
    @APIResponse(responseCode = "200", description = "successfully")
    @APIResponse(responseCode = "409", description = "a conflict has occurred")
    @Tag(name="FORGOT")
    @Path("forgotPass")
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String forgotPass(@FormParam("email") final String email) throws Exception {

        String mail;

        try {
            // check if there is a email in the database
            final User usr = userDAO.find("email", email);

            usr.getVerified().equals(true);
            // generate the hash
            String hashcode = usr.setHash(userDAO.generateHash());

            // send email to user
            usr.getEmail().equals(email);
            String link = "http://localhost:9080/orion-users-service/retrieve?hash=";
            JavaMailUtil.sendMail(email, hashcode, link);
            mail = "connection complete";

        } catch (NoResultException | JwtException | InvalidBuilderException | InvalidClaimException e) {
            mail = "failed to recover password, try again";
        }
        return mail;
    }

    @POST
    @APIResponse(responseCode = "200", description = "successfully")
    @APIResponse(responseCode = "409", description = "a conflict has occurred")
    @Tag(name="FORGOT")
    @Path("changePass")
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String changePass(@FormParam("hash") final String hash, @FormParam("password") final String password)
            throws Exception {

        String text;

        try {
            // check if there is a hash in the database
            final User usr = userDAO.find("hash", hash);
            // send email to user

            // if atribute users's hash is false, cancel change
            usr.getHash().equals(hash);
            usr.setPassword(password);
            String email = usr.getEmail();
            userDAO.update(usr);

            text = "Change password complete!";             
            JavaMailUtil.sendMail(email, null, text);

            usr.setHash(null);


        } catch (NoResultException e) {
            text = "failed to recover password, try again";
        }
        return text;
    }

  

    /**
     * Authenticates the user in the service
     * 
     * @param email    The user's e-mail
     * @param password The user's password
     * 
     * @return Generates a JWT (Json Web Token) or HTTP 404 if fail
     * @throws Exception
     */
    @POST
    @APIResponse(responseCode = "200", description = "successfully")
    @APIResponse(responseCode = "409", description = "a conflict has occurred")
    @Tag(name="CRUD")
    @Path("login")
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String login(@FormParam("email") final String email, @FormParam("password") final String password)
            throws WebApplicationException {
        String jwt = null;
        String message = null;

        try {
            // check if password is correct
            final User user = userDAO.find("email", email);
            if (user != null && user.getPassword().equals(userDAO.MD5(password)) && user.getVerified() == true) {
                // generates the token
                jwt = JwtBuilder.create("jwtBuilder").jwtId(true).claim(Claims.SUBJECT, user.getEmail())
                        .claim("email", user.getEmail()).claim("groups", "users").buildJwt().compact();
            } else {
                message = "The user provides a wrong e-mail or password";
                throw new WebApplicationException("The Wrong password", Response.Status.NOT_FOUND);
            }
        } catch (Exception e) {
            message = "The login method generates a DAO Exception";
            throw new WebApplicationException(message, Response.Status.NOT_FOUND);
        }
        return jwt;
    }

    /**
     * checks if exist email
     * 
     * @param email
     * @return
     */
    private Boolean checkMail(@FormParam("email") final String email) {
        Boolean message;
        try {
            // check if there is a email in the database
            final User usr = userDAO.find("email", email);
            usr.getEmail().equals(email);

            message = true;
        } catch (NoResultException e) {
            message = false;
        }
        return message;
    }

    @GET
    @APIResponse(responseCode ="200", description ="successfully")
    @APIResponse(responseCode ="409", description ="a conflict has occurred")
    @Tag(name="CRUD")
    @Path("/list/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public User read(@PathParam("id") final long id) {
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
    @Path("/delete")
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public User delete(@FormParam("id") final long id) {
        // find the id and delete the user data
        final User usr = userDAO.find(id);
        usr.setVerified(false);
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
    @Path("/update")
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public User update(@FormParam("id") final long id, @FormParam("name") final String name,
            @FormParam("email") final String email, @FormParam("password") final String password)   {

            final User usr = userDAO.find(id);

            if (usr.getVerified() == true) {
    
                if(name.isEmpty() || email.isEmpty() || password.isEmpty()){
                    String message = "error";
                    throw new NotFoundException(message);
                }
                  
                  else {
                    usr.setName(name);
                    usr.setEmail(email);
                    usr.setPassword(password);
                    userDAO.update(usr);
                  }
    
                    return usr;
    
            } else {
                    String message = "error";
                    throw new WebApplicationException(message, Response.Status.CONFLICT);
            }
        }
    

}
