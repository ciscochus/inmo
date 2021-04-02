package com.uoc.inmo.query.filter.inmueble;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InmuebleFilter {
    
    @NonNull
    UUID idStartsWith;
}
