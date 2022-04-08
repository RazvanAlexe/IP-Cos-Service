package com.buyit.orders.controllers;

import com.buyit.cart.DTOs.CartDTO;
import com.buyit.cart.entities.CartEntity;
import com.buyit.orders.entities.OrderEntity;
import com.buyit.orders.DTOs.CreateOrderDTO;
import com.buyit.orders.DTOs.OrderDTO;
import com.buyit.orders.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/makeOrder")
    @Operation(
            summary = "Make a new order",
            description = "Creates a new order and adds it to the list of orders ",
            tags = {"MakeOrder"},
            responses = {
                    @ApiResponse(
                            description = "Created",
                            responseCode = "201",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderEntity.class))
                    ),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            }
    )
    public OrderEntity makeNewOrder(@RequestBody CreateOrderDTO createOrderDTO){
        return this.orderService.makeNewOrder(createOrderDTO);
    }

    @GetMapping("/get/{id}")
    @Operation(
            summary = "Gets the order identified by an id",
            description = "Searches for a order identified by the id param. It returns the requested order",
            tags = {"GetOrderByID"},
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "Order ID. Primary key from dataBase",
                            required = true
                    ),
            },
            responses = {
                    @ApiResponse(
                            description = "OK",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))
                    ),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            }
    )
    public List<OrderDTO> getOrdersByUserId(@PathVariable String id){
        return this.orderService.getOrdersByUserId(id);
    }
}