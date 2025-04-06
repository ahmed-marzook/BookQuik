package com.kaizenflow.bookquik.booking.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.kaizenflow.bookquik.booking.domain.response.InventoryResponse;

@Service
public class InventoryServiceClient {

    @Value("${inventory.service.url}")
    private String inventoryServiceUrl;

    public InventoryResponse getInventory(final Long eventId) {
        final RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(
                inventoryServiceUrl + "/event/" + eventId, InventoryResponse.class);
    }
}
