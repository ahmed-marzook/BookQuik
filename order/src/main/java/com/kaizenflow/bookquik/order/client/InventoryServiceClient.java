package com.kaizenflow.bookquik.order.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.kaizenflow.bookquik.order.exception.InventoryServiceException;

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

    public ResponseEntity<Void> updateInventory(Long eventId, Long ticketCount) {
        log.info("Updating inventory for event ID: {} with ticket count: {}", eventId, ticketCount);

        String url = inventoryServiceUrl + "/event/" + eventId + "/capacity/" + ticketCount;

        try {
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, null, Void.class);
            log.info("Inventory updated successfully for event ID: {}", eventId);
            return response;
        } catch (RestClientException e) {
            log.error("Error updating inventory for event ID: {}", eventId, e);
            throw new InventoryServiceException("Failed to update inventory: " + e.getMessage(), e);
        }
    }
}