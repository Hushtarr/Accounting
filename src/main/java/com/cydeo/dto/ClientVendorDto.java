package com.cydeo.dto;

import com.cydeo.enums.ClientVendorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientVendorDto {
    private Long id;

    @NotBlank(message = "Company Name is required field.")
    @Size(min = 2,max = 50,message = "Last Name must be between 2 and 50 characters long")
    private String clientVendorName;

    @NotBlank(message = "Phone Number is required field and may be in any valid phone number format.")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$")
    private String phone;

    @Pattern(regexp = "^http(s{0,1})://[a-zA-Z0-9/\\-\\.]+.([A-Za-z/] {2,5})[a-zA-Z0-9/\\&\\?\\=\\-\\.\\~\\%]",
            message = "Website should have a valid format.")
    private String website;


    @NotBlank(message = "Please select type.")
    private ClientVendorType clientVendorType;


    private AddressDto address;
    private CompanyDto company;
    private boolean hasInvoice;

}
