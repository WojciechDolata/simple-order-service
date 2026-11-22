package pl.wdolata.simpleorderservice.api.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PlaceOrderRequest(
    @NotNull UUID userId,
    @NotEmpty List<@Valid Item> items,
    @Pattern(regexp = "PLN|EUR") String currency,
    @Pattern(regexp = ".*\\S.*") String couponCode
) {

    public record Item(
        @NotBlank String sku,
        @Min(1) int quantity,
        @DecimalMin("0.01") BigDecimal unitPrice
    ) {
    }
}
