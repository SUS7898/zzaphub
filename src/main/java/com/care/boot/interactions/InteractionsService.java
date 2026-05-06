package com.care.boot.interactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InteractionsService {
    @Autowired private IInteractionsMapper mapper;

    @Transactional
    public int toggleLike(ReactionsDTO dto) {
        if (mapper.checkLikeExists(dto) > 0) {
            mapper.deleteLike(dto);
        } else {
            mapper.insertLike(dto);
        }
        return mapper.getLikeCount(dto.getTargetType(), dto.getTargetId());
    }

    public void submitReport(ReportsDTO dto) {
        mapper.insertReport(dto);
    }
}