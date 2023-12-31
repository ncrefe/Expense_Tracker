package com.project.oba.config;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.project.oba.security.SecurityConstants;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
@Getter
public class SwaggerConfig {

    @Value("${swagger.enabled}")
    private Boolean enabled;

    @Value("${swagger.host}")
    private String host;

    @Value("${swagger.path}")
    private String path;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).host(host).pathMapping(path).enable(enabled)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.project.oba.controller"))
                .paths(Predicates.not(PathSelectors.regex("/_monitoring/*")))
                .build()
                .securitySchemes(Collections.singletonList(securityScheme()))
                .securityContexts(Collections.singletonList(securityContext()));
    }

    private ApiKey securityScheme() {
        return new ApiKey("JWT", SecurityConstants.TOKEN_HEADER, "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("JWT", authorizationScopes));
    }

}
