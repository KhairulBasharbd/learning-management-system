package com.qubex.learn_now.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class SectionCreationRequestDTO {

    private UUID id;
    private String title;
    private String description;
    private int order;
    private UUID courseId;

}
