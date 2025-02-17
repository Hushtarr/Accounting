package com.cydeo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProductDto {

    private Long id;
    @NotNull(message = "Quantity is required field.")
    @Min(value = 1, message = "quantity should be between 1 and 100")
    @Max(value = 100, message = "quantity should be between 1 and 100")
    private Integer quantity;
    @NotNull(message = "Price is a required field.")
    @Min(value = 1, message = "Price should be at least $1")
    private BigDecimal price;
    @NotNull(message = "Tax is a required field.")
    @Min(value = 0, message = "Tax should be between 0% and 20%")
    @Max(value = 20, message = "Tax should be between 0% and 20%")
    private Integer tax;
    private BigDecimal total;
    private BigDecimal profitLoss;
    private Integer remainingQuantity;
    private InvoiceDto invoice;
  
    @Valid
    @NotNull(message = "product name is required field")
    private ProductDto product;

}
