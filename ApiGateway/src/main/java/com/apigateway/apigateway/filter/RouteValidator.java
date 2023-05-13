package com.apigateway.apigateway.filter;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class RouteValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/authenticate"

    );



    public Predicate<ServerHttpRequest> isSecured =
            request -> {
                String path = request.getURI().getPath();
                return openApiEndpoints.stream().noneMatch(uri -> path.contains(uri));
            };
}
