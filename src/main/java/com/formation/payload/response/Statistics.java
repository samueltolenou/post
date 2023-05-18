package com.formation.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statistics {
    boolean success;
    String message;
    List<KeyValue> statistics;
}
