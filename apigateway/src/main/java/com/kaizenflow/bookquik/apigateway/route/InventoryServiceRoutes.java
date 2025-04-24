package com.kaizenflow.bookquik.apigateway.route;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class InventoryServiceRoutes {

    @Value("${service.inventory.url}")
    private String inventoryServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> inventoryRoutes() {
        return GatewayRouterFunctions.route("inventory-service")
                // Event endpoints
                .route(
                        RequestPredicates.POST("/api/v1/inventory/event"),
                        HandlerFunctions.http(inventoryServiceUrl + "/api/v1/inventory/event"))
                .route(
                        RequestPredicates.GET("/api/v1/inventory/events"),
                        HandlerFunctions.http(inventoryServiceUrl + "/api/v1/inventory/events"))
                .route(
                        RequestPredicates.GET("/api/v1/inventory/event/{eventId}"),
                        request -> forwardWithPathVariable(
                                request, "eventId", inventoryServiceUrl + "/api/v1/inventory/event/"))
                .route(
                        RequestPredicates.PUT("/api/v1/inventory/event/{eventId}/capacity/{capacity}"),
                        request -> forwardWithMultiplePathVariables(
                                request,
                                new String[] {"eventId", "capacity"},
                                inventoryServiceUrl + "/api/v1/inventory/event/",
                                "/capacity/"))

                // Venue endpoints
                .route(
                        RequestPredicates.POST("/api/v1/inventory/venue"),
                        HandlerFunctions.http(inventoryServiceUrl + "/api/v1/inventory/venue"))
                .route(
                        RequestPredicates.GET("/api/v1/inventory/venues"),
                        HandlerFunctions.http(inventoryServiceUrl + "/api/v1/inventory/venues"))
                .route(
                        RequestPredicates.GET("/api/v1/inventory/venue/{venueId}"),
                        request -> forwardWithPathVariable(
                                request, "venueId", inventoryServiceUrl + "/api/v1/inventory/venue/"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceApiDocsRoutes() {
        return GatewayRouterFunctions.route("inventory-service-api-docs")
                // Booking endpoint
                .route(
                        RequestPredicates.path("/docs/inventoryservice/v3/api-docs"),
                        HandlerFunctions.http(inventoryServiceUrl))
                .filter(setPath("/v3/api-docs"))
                .build();
    }

    private static ServerResponse forwardWithPathVariable(ServerRequest request, String pathVariable, String baseUrl)
            throws Exception {
        String value = request.pathVariable(pathVariable);
        return HandlerFunctions.http(baseUrl + value).handle(request);
    }

    private static ServerResponse forwardWithMultiplePathVariables(
            ServerRequest request, String[] pathVariables, String baseUrl, String middleUrl) throws Exception {
        String firstValue = request.pathVariable(pathVariables[0]);
        String secondValue = request.pathVariable(pathVariables[1]);
        return HandlerFunctions.http(baseUrl + firstValue + middleUrl + secondValue)
                .handle(request);
    }
}
