package com.cydeo.dto;

import com.cydeo.enums.CompanyStatus;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDto {
    private Long id;

    @NotBlank(message = "Title is a required field")
    @Size(max = 100, min = 2, message = "Title must be between 2 and 100 characters long")
 //   @UniqueElements( message = "This title already exists")
    private String title;

    @NotBlank(message = "Phone Number is a required field")
    @Pattern(regexp= "^(\\+\\d{1,2}\\s?)?1?\\-?\\.?\\s?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$", message= " can be in any valid phone number format")
    private String phone;

    @Pattern(regexp = "^(https?:\\/\\/)?([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}(:\\d{1,5})?(\\/[a-zA-Z0-9\\.\\,\\?\\'\\/\\\\\\+&%\\$#_=~\\-]*)?$\n", message = "Website should have a valid format")
    private String website;

    @Valid
    @NotNull
    private AddressDto address;

    private CompanyStatus companyStatus;
}
