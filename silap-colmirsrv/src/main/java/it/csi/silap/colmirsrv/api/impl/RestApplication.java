/*-
 * ========================LICENSE_START=================================
 * colmirsrv
 * %%
 * Copyright (C) 2024 Regione Piemonte
 * %%
 * SPDX-FileCopyrightText: Copyright 2024 | Regione Piemonte
 * SPDX-License-Identifier: EUPL-1.2-or-later
 * =========================LICENSE_END==================================
 */
package it.csi.silap.colmirsrv.api.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.Provider;

import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;


@ApplicationPath("/api/v1")
@Provider
public class RestApplication extends Application {

	
	@SuppressWarnings("rawtypes")
	public RestApplication() {
        super();
        OpenAPI oas = new OpenAPI();
        
        oas.components(new Components().addSchemas("Map", new Schema<Map<String, Object>>().addProperty("< * >", new ObjectSchema())));
        
        Info info = new Info()
            .title("colmirsrv")
            .description("API per colmirsrv")
            .version("1.0.0");
        oas.info(info);
        
        
        Set<String> resourcePackages = new HashSet<String>();
        resourcePackages.add("it.csi.silap.colmirsrv.api");
        
        SwaggerConfiguration oasConfig = new SwaggerConfiguration()
            .openAPI(oas)
            .prettyPrint(true)
            .resourcePackages(resourcePackages);    

        try {
            new JaxrsOpenApiContextBuilder()
                .application(this)
                .openApiConfiguration(oasConfig)
                .buildContext(true);
        } catch (OpenApiConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }
	
}
