package com.uoc.inmo.command.api.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestFile {
    
    private UUID id;
    private String name;
    private String mimeType;
    private String content;

    public RequestFile(String name, String mimeType, String content) {
        this.id = UUID.randomUUID();
        
        this.name = name;
        this.mimeType = mimeType;
        this.content = content;
    }

    
}
