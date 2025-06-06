package org.itltcanz.tms.service;

import lombok.AllArgsConstructor;
import org.itltcanz.tms.entity.StatusEntity;
import org.itltcanz.tms.exceptions.EntityException;
import org.itltcanz.tms.repository.StatusRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StatusService {
    private final StatusRepository statusRepository;
    public StatusEntity findById(Integer statusId) {
        return statusRepository.findById(statusId).orElseThrow(() -> new EntityException("Status not found"));
    }
}
