package com.BookingManagementService.modeldemo.feignclient;

import org.example.model.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="Payment-System")
public interface Payable {
    @PostMapping("/Payment/pay")
    public PaymentRequest pay(@RequestBody PaymentRequest paymentRequest);
}
