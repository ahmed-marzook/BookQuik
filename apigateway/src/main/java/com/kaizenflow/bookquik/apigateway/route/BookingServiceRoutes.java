package com.kaizenflow.bookquik.apigateway.route;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

@Configuration
public class BookingServiceRoutes {

    @Value("${service.booking.url}")
    private String bookingServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> bookingRoutes() {
        return GatewayRouterFunctions.route("booking-service")
                // Booking endpoint
                .route(
                        RequestPredicates.POST("/api/v1/book"),
                        HandlerFunctions.http(bookingServiceUrl + "/api/v1/book"))

                // Customer endpoints
                .route(
                        RequestPredicates.GET("/api/v1/customers"),
                        HandlerFunctions.http(bookingServiceUrl + "/api/v1/customers"))
                .route(
                        RequestPredicates.POST("/api/v1/customers"),
                        HandlerFunctions.http(bookingServiceUrl + "/api/v1/customers"))
                .route(
                        RequestPredicates.GET("/api/v1/customers/{id}"),
                        request -> forwardWithPathVariable(request, "id", bookingServiceUrl + "/api/v1/customers/"))
                .route(
                        RequestPredicates.PUT("/api/v1/customers/{id}"),
                        request -> forwardWithPathVariable(request, "id", bookingServiceUrl + "/api/v1/customers/"))
                .route(
                        RequestPredicates.DELETE("/api/v1/customers/{id}"),
                        request -> forwardWithPathVariable(request, "id", bookingServiceUrl + "/api/v1/customers/"))
                .route(
                        RequestPredicates.GET("/api/v1/customers/email"),
                        HandlerFunctions.http(bookingServiceUrl + "/api/v1/customers/email"))

                .filter(CircuitBreakerFilterFunctions.circuitBreaker("bookingServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return GatewayRouterFunctions.route("fallbackRoute").POST("/fallbackRoute", request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE).body("Booking Service is down")).build();
    }

    private static ServerResponse forwardWithPathVariable(ServerRequest request, String pathVariable, String baseUrl)
            throws Exception {
        String value = request.pathVariable(pathVariable);
        return HandlerFunctions.http(baseUrl + value).handle(request);
    }
}
