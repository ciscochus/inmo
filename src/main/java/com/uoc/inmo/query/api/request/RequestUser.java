package com.uoc.inmo.query.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RequestUser {
    
    @ApiModelProperty(name = "email")
    private String email;

    @ApiModelProperty(name = "password")
    private String password;

    @ApiModelProperty(name = "tipo", allowableValues = "PROFESIONAL, PARTICULAR")
    private String tipo;
}
