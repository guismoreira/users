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

package orion.user.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import orion.user.data.UserDAO;
import orion.user.model.User;

import java.util.List;
import org.eclipse.microprofile.jwt.JsonWebToken;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ibm.websphere.security.jwt.Claims;
import com.ibm.websphere.security.jwt.InvalidBuilderException;
import com.ibm.websphere.security.jwt.InvalidClaimException;
import com.ibm.websphere.security.jwt.JwtBuilder;
import com.ibm.websphere.security.jwt.JwtException;

import org.jsoup.Jsoup;

@RequestScoped
@Path("/api/v1.0/")
public class ServiceController {

    @Inject
    private UserDAO userDAO;

        // The JWT of the current caller. Since this is a request scoped resource, the
    // JWT will be injected for each JAX-RS request. The injection is performed by
    // the mpJwt-1.0 feature.
    @Inject
    private JsonWebToken jwt;

    // @POST
    // @Path("/jwt")
    // @Produces(MediaType.APPLICATION_JSON)
    // @Transactional
    // @RolesAllowed({ "users" })
    // public void testJwt() {
    //     System.out.println("jwt " + this.jwt.getClaim("upn"));
    //     System.out.println("jwt " + this.jwt.getClaim("pass"));
    // }

@POST
@Path("/jwt")
@Consumes("application/x-www-form-urlencoded")
@Produces(MediaType.TEXT_PLAIN)
@Transactional
public String login(
    @FormParam("email") final String email, 
    @FormParam("password") final String password) {
// 2 - check if the users exists in the data base

// 3 - if yes, generates the token
    String jwt = null;
    User usr;
    try {   
        usr = userDAO.find("email",email);
        usr = userDAO.find("password",password);
        if(usr.getEmail() == email && usr.getPassword() == password) {
            jwt = JwtBuilder.create("jwtBuilder")
            .jwtId(true)
            .claim(Claims.SUBJECT, email)
            .claim("email", email)
            .claim("password", password)
            .claim("groups", "users")
            .buildJwt().compact();
            System.out.println(jwt);
            }
            
    } catch (JwtException | InvalidBuilderException | InvalidClaimException e) {
        e.printStackTrace();
    }
    return jwt;

 
}





    @POST // create the user with name and email
    @Path("/createusers/")
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public User create(
        @FormParam("name") final String name,
        @FormParam("email") final String email,
        @FormParam("password") final String password) {
        final User usr = new User();
            usr.setName(name);
            usr.setEmail(email);
            usr.setPassword(password);
            userDAO.create(usr);
            return usr; //return a User objetct
        
     }



    @GET //list a user
    @Path("/listusers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public User read(@PathParam("id") final long id) {
        return userDAO.find(id);
    }

     

    @POST // delete a user
    @Path("/deleteusers")
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public void delete(@FormParam("id") final long id) {
        userDAO.delete(id); // find the id and delete the user data
        
    }

    @POST // update a user
    @Path("/updateusers")
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public User update(
        @FormParam("id") final long id,
        @FormParam("name") final String name,
        @FormParam("email") final String email) {
            User usr = userDAO.find(id);
            usr.setName(name);
            usr.setEmail(email);
            userDAO.update(usr);
            return usr;
    }



    



}
