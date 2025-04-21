package com.kaizenflow.bookquik.booking.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kaizenflow.bookquik.booking.domain.response.InventoryResponse;
import com.kaizenflow.bookquik.booking.exception.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InventoryServiceClient {

    private final RestTemplate restTemplate;
    private final String inventoryServiceUrl;

    public InventoryServiceClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${inventory.service.url}") String inventoryServiceUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.inventoryServiceUrl = inventoryServiceUrl;
    }

    public InventoryResponse getInventory(final Long eventId) {
        log.info("Fetching inventory for event ID: {}", eventId);

        try {
            InventoryResponse response =
                    restTemplate.getForObject(
                            inventoryServiceUrl + "/event/" + eventId, InventoryResponse.class);

            if (response == null) {
                throw new ResourceNotFoundException("Event", "id", eventId);
            }

            return response;
        } catch (Exception e) {
            log.error("Error fetching inventory for event ID: {}", eventId, e);
            if (e instanceof ResourceNotFoundException) {
                throw e;
            }
            throw new RuntimeException(
                    "Error communicating with inventory service: " + e.getMessage(), e);
        }
    }
}
