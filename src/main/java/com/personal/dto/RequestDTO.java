package com.personal.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestDTO {
    private String transaccionId;
    private List<ParametroDTO> parametros;
}
