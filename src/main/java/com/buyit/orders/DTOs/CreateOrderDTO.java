package com.buyit.orders.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateOrderDTO {
    private int shippingAddressId;
    private int billingAddressId;
}
