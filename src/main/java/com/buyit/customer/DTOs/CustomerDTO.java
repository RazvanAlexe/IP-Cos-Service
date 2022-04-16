package com.buyit.customer.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerDTO {
    private String username;
    private String firstname;
    private String lastname;
}