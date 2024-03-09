package com.example.apigateway.apigateway.config;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RoleBasedFilter extends AbstractGatewayFilterFactory<RoleBasedFilter.Config> {
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String token = request.getHeaders().getOrEmpty("Authorization").get(0).substring(7);

            Claims claims = jwtUtil.getAllClaimsFromToken(token);
            List<Map<String, String>> rolesFromJwt = (List<Map<String, String>>) claims.get("roles");
            List<String> roles = rolesFromJwt.stream()
                    .map(role-> role.get("authority"))
                    .collect(Collectors.toList());

            List<String> defaultRoles = config.getRequiredRoles();
            if(hasRequiredRoles(roles, defaultRoles)){
                return chain.filter(exchange);
            }
            else{
                HttpStatus statusCode = HttpStatus.FORBIDDEN;
                exchange.getResponse().setStatusCode(statusCode);

                // Create the response body
                String responseBody = "Custom message: " + statusCode.value() + " - " + statusCode.getReasonPhrase();
                byte[] responseBytes = responseBody.getBytes();

                // Set the response body and complete
                return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(responseBytes)))
                        .then(Mono.fromRunnable(() -> exchange.getResponse().setComplete()));
            }

        };

    }
    public boolean hasRequiredRoles(List<String>roles, List<String>defaultRoles){
        for(String role:defaultRoles){
            if(roles.contains(role)){
                return true;
            }
        }
        return false;
    }

    public static class Config {
        private List<String> requiredRoles;

        public Config(List<String> requiredRoles){
            this.requiredRoles =requiredRoles;
        }

        public List<String> getRequiredRoles() {
            return requiredRoles;
        }

    }
}