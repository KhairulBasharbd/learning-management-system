package com.qubex.learn_now.dto.response;

import lombok.Data;

import java.util.UUID;

@Data

public class LectureDTO {
    private UUID id;
    private String title;
    private String content;
    private String videoUrl;
    private int order;
    private boolean completed;
}
