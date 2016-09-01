/*
 * Copyright 2015-2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hawkular.client.test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hawkular.client.core.ClientResponse;
import org.hawkular.client.core.HawkularClient;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    private HawkularClient client;
    public static final long MINUTE = 1000 * 60;

    @BeforeClass
    public void init() throws Exception {
        URI endpoint = getEndpointFromEnv();
        Reporter.log(endpoint.toString());

        HashMap<String, Object> headers = new HashMap<String, Object>();
        headers.put(HawkularClient.KEY_HEADER_TENANT, "unit-testing");

        client = new HawkularClient(endpoint, getUsername(), getPassword(), headers);
    }

    private URI getEndpointFromEnv() throws URISyntaxException {
        String endpoint = System.getenv("HAWKULAR_ENDPOINT");
        if (StringUtils.trimToNull(endpoint) == null) {
            Reporter.log("HAWKULAR_ENDPOINT env not defined. Defaulting to 'localhost'");
            endpoint = "http://localhost:8080";
        }

        return new URI(endpoint);
    }

    private String getUsername() {
        String username = System.getenv("HAWKULAR_USER");
        if (StringUtils.trimToNull(username) == null) {
            Reporter.log("HAWKULAR_USER env not defined. Defaulting to 'jdoe'");
            username = "jdoe";
        }

        return username;
    }

    private String getPassword() {
        String password = System.getenv("HAWKULAR_PASSWORD");
        if (StringUtils.trimToNull(password) == null) {
            Reporter.log("HAWKULAR_PASSWORD env not defined. Defaulting to 'password'");
            password = "password";
        }

        return password;
    }

    /**
     * Return the main Hawkular client
     *
     * @return HawkularClient
     */
    public HawkularClient client() {
        return client;
    }

    @BeforeMethod
    public void isHawkularAlive() {
        ClientResponse<Map<String, String>> response = client.metrics().ping().ping();

        Assert.assertNotNull(response);
        Assert.assertTrue(response.isSuccess());
    }
}
