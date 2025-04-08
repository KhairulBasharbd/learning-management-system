package com.qubex.learn_now.dto.request;

import com.qubex.learn_now.enums.LectureType;
import lombok.Data;

import java.util.UUID;

@Data
public class LectureCreationRequestDTO {

    private UUID id;
    private String title;
    private String description;
    private int order;
    private LectureType type;
    private String content;
    private UUID sectionId;
}
