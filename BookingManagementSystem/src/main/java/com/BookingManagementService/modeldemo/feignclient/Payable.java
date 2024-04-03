package com.BookingManagementService.modeldemo.feignclient;

import org.example.model.CustomerRequest;
import org.example.model.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="Payment-System")
public interface Payable {
    @PostMapping("/Payment/pay")
    public PaymentRequest pay(@RequestBody PaymentRequest paymentRequest);

    @PostMapping("/Payment/getBalance")
    public double get(@RequestBody CustomerRequest customerRequest);
}
