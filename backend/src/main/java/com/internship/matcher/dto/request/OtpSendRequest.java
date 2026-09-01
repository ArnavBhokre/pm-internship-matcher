package com.internship.matcher.dto.request;

import com.internship.matcher.model.enums.Enums.OtpPurpose;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OtpSendRequest {
    @NotBlank @Email
    private String email;

    @NotNull
    private OtpPurpose purpose;
}
