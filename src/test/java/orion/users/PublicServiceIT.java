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
package orion.users;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;

import orion.users.model.User;

/**
 * Integration test
 * 
 * The test can be done with the service's database or one generated specially
 * for each test. The ID generated in the create method is random, so it is
 * recommended to test it in the service database, in which you can know which
 * IDs can be tested in the other methods
 * 
 *
 * docker-compose up -d mvn liberty:dev enter
 *
 * or
 *
 * mvn verify
 */

@ExtendWith({ DockerCompose.class })

@TestMethodOrder(OrderAnnotation.class)
public class PublicServiceIT {


    private static String API = "/orion-users-service/users/api/v1/";
    private String host;
    private Integer port;
    private CloseableHttpClient client;

    public PublicServiceIT() {
        this.client = HttpClients.createDefault();
        host = DockerCompose.users.getContainerIpAddress();
        port = DockerCompose.users.getFirstMappedPort();
    }

    @Test
    @Order(1)
    public void testCreate() {
        try {
            // Mounting URL, create
            String url = "http://" + host + ":" + port + API + "create";
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", "mike"));
            params.add(new BasicNameValuePair("email", "testorionservice@outlook.com"));
            params.add(new BasicNameValuePair("password", "thepassword"));
            post.setEntity(new UrlEncodedFormEntity(params));
            // execute and getting the response
            HttpResponse response = this.client.execute(post);
            // Get response body
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            // Jsonb jsonb = JsonbBuilder.create();
            // this.user = jsonb.fromJson(content, User.class);
            System.out.println("testCreate >>>>>>>>>>>>" + content);

            assertEquals(response.getStatusLine().getStatusCode(), 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test   
    @Order(2)
    public void testAutoConfirm() {
        try {
            // Mounting URL
            String url = "http://" + host + ":" + port + API + "autoConfirm";
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //insert below an id already registered in the database
            params.add(new BasicNameValuePair("email", "testorionservice@outlook.com"));
            post.setEntity(new UrlEncodedFormEntity(params));
            // execute and getting the response
            HttpResponse response = this.client.execute(post);
            //Get response body
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            System.out.println("testAutoConfirm >>>>>>>>>>>>" + content);
 
            assertEquals(response.getStatusLine().getStatusCode(), 200);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test   
    @Order(3)
    public void testUpdate() {
        try {
            // Mounting URL
            String url = "http://" + host + ":" + port + API + "update";
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //insert below an id already registered in the database
            params.add(new BasicNameValuePair("id", "1000"));
            params.add(new BasicNameValuePair("name", "joe"));
            params.add(new BasicNameValuePair("email", "testorionservice@outlook.com"));
            params.add(new BasicNameValuePair("password", "the_password"));
            post.setEntity(new UrlEncodedFormEntity(params));
            // execute and getting the response
            HttpResponse response = this.client.execute(post);
            //Get response body
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            System.out.println("testUpdate >>>>>>>>>>>>" + content);
 
            assertEquals(response.getStatusLine().getStatusCode(), 200);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(4)
    public void testCreate2() {
        try {
            // Mounting URL, create
            String url = "http://" + host + ":" + port + API + "create";
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", "jonas"));
            params.add(new BasicNameValuePair("email", "guilhermemoreiramex@gmail.com"));
            params.add(new BasicNameValuePair("password", "thepassword"));
            post.setEntity(new UrlEncodedFormEntity(params));
            // execute and getting the response
            HttpResponse response = this.client.execute(post);
            // Get response body
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            // Jsonb jsonb = JsonbBuilder.create();
            // this.user = jsonb.fromJson(content, User.class);
            System.out.println("testCreate2 >>>>>>>>>>>>" + content);

            assertEquals(response.getStatusLine().getStatusCode(), 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test   
    @Order(5)
    public void testAutoConfirm2() {
        try {
            // Mounting URL
            String url = "http://" + host + ":" + port + API + "autoConfirm";
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //insert below an id already registered in the database
            params.add(new BasicNameValuePair("email", "guilhermemoreiramex@gmail.com"));
            post.setEntity(new UrlEncodedFormEntity(params));
            // execute and getting the response
            HttpResponse response = this.client.execute(post);
            //Get response body
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            System.out.println("testAutoConfirm2 >>>>>>>>>>>>" + content);
 
            assertEquals(response.getStatusLine().getStatusCode(), 200);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Test
    @Order(6)
    public void testRead() {
        try { 
            // Mounting URL
            //insert below an id already registered in the database
            String url = "http://" + host + ":" + port + API + "list/1001";
            HttpGet get = new HttpGet(url);
            // execute and getting the response
            HttpResponse response = this.client.execute(get);
            //Get response body
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            System.out.println("testRead >>>>>>>>>>>>" + content);

            assertEquals(response.getStatusLine().getStatusCode(), 200);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(7)
    public void testLogin() {
        try {
            // Mounting URL, create
            String url = "http://" + host + ":" + port + API + "login";
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", "guilhermemoreiramex@gmail.com"));
            params.add(new BasicNameValuePair("password", "thepassword"));
            post.setEntity(new UrlEncodedFormEntity(params));
            // execute and getting the response
            HttpResponse response = this.client.execute(post);
            // Get response body
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            // Jsonb jsonb = JsonbBuilder.create();
            // this.user = jsonb.fromJson(content, User.class);
            System.out.println("testLogin >>>>>>>>>>>>" + content);

            assertEquals(response.getStatusLine().getStatusCode(), 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(8)
    public void testForgotpass() {
        try {
            // Mounting URL, create
            String url = "http://" + host + ":" + port + API + "forgotPass";
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", "guilhermemoreiramex@gmail.com"));
            post.setEntity(new UrlEncodedFormEntity(params));
            // execute and getting the response
            HttpResponse response = this.client.execute(post);
            // Get response body
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            // Jsonb jsonb = JsonbBuilder.create();
            // this.user = jsonb.fromJson(content, User.class);
            System.out.println("testForgotpass >>>>>>>>>>>>" + content);

            assertEquals(response.getStatusLine().getStatusCode(), 200);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//     @Test   
//     @Order(9)
//     public void testDelete() {
//        try {

          
//            // Mounting URL, create
//            String url = "http://" + host + ":" + port + API + "delete";


          
//            HttpPost post = new HttpPost(url);

//            List<NameValuePair> params = new ArrayList<NameValuePair>();

//            //insert below an id already registered in the database
//            params.add(new BasicNameValuePair("id", "1001"));
//            post.setEntity(new UrlEncodedFormEntity(params));

//            // execute and getting the response
//            HttpResponse response = this.client.execute(post);


//            //Get response body
//            HttpEntity entity = response.getEntity();
//            String content = EntityUtils.toString(entity);
//            System.out.println("testDelete >>>>>>>>>>>>" + content);
//            System.out.println("testDelete code >>>>>>>>>>>>" + response.getStatusLine().getStatusCode() );

//            assertEquals(response.getStatusLine().getStatusCode(), 200);
           
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /*
    @Test
    @Order(10)
    public void testFailCreate() {
        try {
            // Mounting URL, create
            String url = "http://" + host + ":" + port + API + "create";
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", ""));
            params.add(new BasicNameValuePair("email", ""));
            params.add(new BasicNameValuePair("password", ""));
            post.setEntity(new UrlEncodedFormEntity(params));
            // execute and getting the response
            HttpResponse response = this.client.execute(post);
            // Get response body
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            // Jsonb jsonb = JsonbBuilder.create();
            // this.user = jsonb.fromJson(content, User.class);
            System.out.println("testFailCreate >>>>>>>>>>>>" + content);

            assertEquals(response.getStatusLine().getStatusCode(), 404);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(11)
    public void testFailCreate2() {
        try {
            // Mounting URL, create
            String url = "http://" + host + ":" + port + API + "create";
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", "tyson"));
            params.add(new BasicNameValuePair("email", "testorionservice@outlook.com"));
            params.add(new BasicNameValuePair("password", "password"));
            post.setEntity(new UrlEncodedFormEntity(params));
            // execute and getting the response
            HttpResponse response = this.client.execute(post);
            // Get response body
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            // Jsonb jsonb = JsonbBuilder.create();
            // this.user = jsonb.fromJson(content, User.class);
            System.out.println("testFailCreate2 >>>>>>>>>>>>" + content);

            assertEquals(response.getStatusLine().getStatusCode(), 409);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(12)
    public void testFailCreate3() {
        try {
            // Mounting URL, create
            String url = "http://" + host + ":" + port + API + "create";
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", "tyson"));
            params.add(new BasicNameValuePair("password", "password"));
            post.setEntity(new UrlEncodedFormEntity(params));
            // execute and getting the response
            HttpResponse response = this.client.execute(post);
            // Get response body
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            // Jsonb jsonb = JsonbBuilder.create();
            // this.user = jsonb.fromJson(content, User.class);
            System.out.println("testFailCreate3 >>>>>>>>>>>>" + content);

            assertEquals(response.getStatusLine().getStatusCode(), 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

}