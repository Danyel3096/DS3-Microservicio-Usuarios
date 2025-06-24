package com.ds3.team8.users_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ds3.team8.users_service.config.FeignClientInterceptor;

@FeignClient(name = "orders-service", configuration = FeignClientInterceptor.class)
public interface OrderClient {
     
    @GetMapping("/api/v1/orders/user/{userId}/exists")
    Boolean userHasOrders(@PathVariable("userId") Long userId);

}
