package com.skipthedishes.courier.controllers;

import java.util.List;

import com.skipthedishes.courier.models.DeliveryStatementDto;
import com.skipthedishes.courier.models.DeliveryTransactionDto;
import com.skipthedishes.courier.services.DeliveryTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class DeliveryTransactionController {

    private final DeliveryTransactionService deliveryTransactionService;

    @Operation(summary = "Find the delivery transaction by courier id in a given period.",
        description = "A delivery transaction is the sum of all values (delivery, adjustments and bonuses) for a "
            + "given deliveryId.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Delivery Transactions found.",
        content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = DeliveryTransactionDto.class))})})
    @GetMapping("/transactions")
    public List<DeliveryTransactionDto> transactions(@RequestParam final int courierId, @RequestParam final long start,
        @RequestParam final long end) {
        return deliveryTransactionService.findTransactionsByCourierAndDate(courierId, start, end);
    }

    @Operation(summary = "Retrieve the weekly courier statement for a specific courier",
        description = "The weekly courier statement is the sum of all delivery transactions.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Delivery Statement found.",
        content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = DeliveryStatementDto.class))}),
        @ApiResponse(responseCode = "404", description = "The courier id given does not exist ",
            content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/statement")
    public DeliveryStatementDto statement(@RequestParam final int courierId) {
        return deliveryTransactionService.calculateStatement(courierId);
    }
}
