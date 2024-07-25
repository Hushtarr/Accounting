package com.cydeo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProductDto {

    private Long id;
    @NotBlank(message = "quantity is required field.")
    @Size(min = 1,max = 100,message = "Quantity cannot be greater than 100 or less than 1")
    private Integer quantity;
    @NotBlank(message = "Price is a required field.")
    @Min(value = 1, message = "Price should be at least $1")
    private BigDecimal price;
    @NotBlank(message = "tax is a required field")
    @Min(value = 0, message = "Tax should be between 0% and 20%")
    @Max(value = 20, message = "Tax should be between 0% and 20%")
    private Integer tax;
    private BigDecimal total;
    private BigDecimal profitLoss;
    private Integer remainingQuantity;
    private InvoiceDto invoice;
    private ProductDto product;

}
