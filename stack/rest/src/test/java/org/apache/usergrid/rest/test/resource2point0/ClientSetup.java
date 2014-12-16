/**
 * Created by ApigeeCorporation on 12/4/14.
 */
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.usergrid.rest.test.resource2point0;


import java.io.IOException;
import java.util.Map;

import org.apache.usergrid.rest.test.resource2point0.endpoints.ApplicationResource;
import org.apache.usergrid.rest.test.resource2point0.endpoints.OrganizationResource;
import org.apache.usergrid.rest.test.resource2point0.model.Application;
import org.apache.usergrid.rest.test.resource2point0.model.Token;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import org.apache.usergrid.persistence.index.utils.UUIDUtils;
import org.apache.usergrid.rest.test.resource2point0.model.ApiResponse;
import org.apache.usergrid.rest.test.resource2point0.model.Organization;
import org.apache.usergrid.rest.test.security.TestAdminUser;
import org.apache.usergrid.utils.MapUtils;


/**
 * This class is used to setup the client rule that will setup the RestClient and create default applications.
 */
public class ClientSetup implements TestRule {

    RestClient restClient;
    protected String username, orgName, appName;
    protected Organization organization;
    protected Application application;


    public ClientSetup (String serverUrl) {

        restClient = new RestClient( serverUrl );
    }

    public Statement apply( Statement base, Description description ) {
        return statement( base, description );
    }


    private Statement statement( final Statement base, final Description description ) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                before( description );
                try {
                    base.evaluate();
                }
                finally {
                    cleanup();
                }
            }
        };
    }


    protected void cleanup() {
        // might want to do something here later
    }


    protected void before( Description description ) throws IOException {
        String testClass = description.getTestClass().getName();
        String methodName = description.getMethodName();
        String name = testClass + "." + methodName;

        username = "user_"+name + UUIDUtils.newTimeUUID();
        orgName = "org_"+name+UUIDUtils.newTimeUUID();
        orgName = "app_"+name+UUIDUtils.newTimeUUID();

        organization = restClient.management().orgs().post(new Organization( orgName,username,username+"@usergrid.com",username,username, null  ));

        restClient.management().token().post(new Token(username,username));

        restClient.management().orgs().organization(organization.getName()).app().post(new Application(appName));

        System.out.println();

    }

    public String getUsername(){return username;}

    public Organization getOrganization(){return organization;}

    public String getOrganizationName(){return orgName;}

    public String getAppName() {return appName;}


    public RestClient getRestClient(){
        return restClient;
    }
}