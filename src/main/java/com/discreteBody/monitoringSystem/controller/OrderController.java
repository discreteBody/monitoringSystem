package com.discreteBody.monitoringSystem.controller;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class OrderController {

    private final Counter orderCounter;
    private final Random random = new Random();

    // Inject MeterRegistry via constructor
    public OrderController(MeterRegistry registry) {
        // Define a custom counter metric with a tag
        this.orderCounter = registry.counter("business.orders.total", "type", "online");
    }

    @GetMapping("/api/order")
    @Timed(value = "business.order.processing.time", description = "Time taken to process an order", percentiles = {0.95, 0.99})
    public String createOrder() throws InterruptedException {
        // 1. Increment the custom business counter
        orderCounter.increment();

        // 2. Simulate some random processing delay to make our P99 graphs look realistic
        int delay = random.nextInt(100) + 10; // 10ms to 110ms delay

        // Occasionally simulate a "long tail" latency spike (the 1% that makes P99 important)
        if (random.nextInt(100) > 95) {
            delay += 500;
        }

        Thread.sleep(delay);

        return "Order processed successfully!";
    }
}