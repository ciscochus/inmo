package com.uoc.inmo.notification.api.request;

import java.util.UUID;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestMessage {
    
    @ApiModelProperty(name = "from")
    private String from;

    @ApiModelProperty(name = "to")
    private String to;

    @ApiModelProperty(name = "message")
    private String message;

    @ApiModelProperty(name = "inmuebleId")
    private UUID inmuebleId;
}
