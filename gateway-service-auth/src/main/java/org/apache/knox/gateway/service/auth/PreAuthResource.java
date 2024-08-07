/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.knox.gateway.service.auth;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path(PreAuthResource.RESOURCE_PATH)
public class PreAuthResource extends AbstractAuthResource {

  static final String RESOURCE_PATH = "auth/api/v1/pre";

  @Context
  HttpServletResponse response;

  @Context
  ServletContext context;
  @PostConstruct
  public void init() {
    initialize();
  }

  @Override
  HttpServletResponse getResponse() {
    return response;
  }

  @Override
  ServletContext getContext() {
    return context;
  }

  @GET
  public Response doGet() {
    return doGetImpl();
  }

  @PUT
  public Response doPut() {
    return doGetImpl();
  }

  @POST
  public Response doPost() {
    return doGetImpl();
  }

  @PATCH
  public Response doPatch() {
    return doGetImpl();
  }

  @DELETE
  public Response doDelete() {
    return doGetImpl();
  }

}
