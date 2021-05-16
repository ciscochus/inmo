package com.uoc.inmo.query.api.response;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseFile {
    
    private UUID id;
    private String name;
    private String mimeType;
    private String content;

    public ResponseFile(String name, String mimeType, String content) {
        this.id = UUID.randomUUID();
        
        this.name = name;
        this.mimeType = mimeType;
        this.content = content;
    }

    public ResponseFile(UUID id, String name, String mimeType, String content) {
        this.id = id;
        this.name = name;
        this.mimeType = mimeType;
        this.content = content;
    }
}
