package com.buyit.shipping.address.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShippingAddressDTO {
    private String city;
    private String address;
}
