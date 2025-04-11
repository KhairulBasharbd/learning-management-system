package com.qubex.learn_now.dto;

import com.qubex.learn_now.dto.response.LectureDTO;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class SectionDTO {
    private UUID id;
    private String title;
    private String description;
    private int order;
    private List<LectureDTO> lessons;
}
