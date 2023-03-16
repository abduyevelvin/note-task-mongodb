package com.task.note.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class JwtTokenDTO {
    private String subject;
    private Date expirationDate;
    private String role;
}
