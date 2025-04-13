package com.qubex.learn_now.service;

import com.qubex.learn_now.dto.request.LectureCreationRequestDTO;
import com.qubex.learn_now.enums.LectureType;
import com.qubex.learn_now.exception.custom.NotFoundException;
import com.qubex.learn_now.model.Lecture;
import com.qubex.learn_now.model.Section;
import com.qubex.learn_now.repository.LectureRepository;
import com.qubex.learn_now.repository.SectionRepository;
import com.qubex.learn_now.util.VideoUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final SectionRepository sectionRepository;

    @Transactional
    public Lecture createLecture(LectureCreationRequestDTO lectureDTO) {
        Section section = sectionRepository.findById(lectureDTO.getSectionId())
                .orElseThrow(() -> new NotFoundException("Section not found"));

        Lecture lecture = new Lecture();
        lecture.setTitle(lectureDTO.getTitle());
        lecture.setDescription(lectureDTO.getDescription());
        lecture.setType(lectureDTO.getType());
        lecture.setContent(lectureDTO.getContent());
        lecture.setSection(section);

        if(lectureDTO.getType() == LectureType.VIDEO){

            String embedUrl = VideoUtil.convertToEmbedUrl(lectureDTO.getContent());
            lecture.setEmbedUrl(embedUrl);
        }


        // Get max order and add 1
        int maxOrder = lectureRepository.findBySectionIdOrderByOrderAsc(section.getId())
                .stream().mapToInt(Lecture::getOrder).max().orElse(0);
        lecture.setOrder(maxOrder + 1);

        return lectureRepository.save(lecture);
    }

    @Transactional
    public List<Lecture> getLecturesBySectionId(UUID sectionId) {
        return lectureRepository.findBySectionIdOrderByOrderAsc(sectionId);
    }


}
