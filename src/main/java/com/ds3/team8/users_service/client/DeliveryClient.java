package com.ds3.team8.users_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ds3.team8.users_service.config.FeignClientInterceptor;

@FeignClient(name = "deliveries-service", configuration = FeignClientInterceptor.class)
public class DeliveryClient {
    
    @GetMapping("/api/v1/deliveries/user/{userId}/exists")
    Boolean userHasDeliveries(@PathVariable("userId") Long userId);
}
