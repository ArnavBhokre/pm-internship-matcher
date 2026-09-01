package com.internship.matcher.dto.request;

import com.internship.matcher.model.enums.Enums.OtpPurpose;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OtpVerifyRequest {
    @NotBlank @Email
    private String email;

    @NotBlank
    private String otpCode;

    @NotNull
    private OtpPurpose purpose;
}
