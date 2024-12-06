package org.itltcanz.tms.service;

import lombok.AllArgsConstructor;
import org.itltcanz.tms.entity.Priority;
import org.itltcanz.tms.exceptions.EntityException;
import org.itltcanz.tms.repository.PriorityRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PriorityService {
    private final PriorityRepository priorityRepository;
    public Priority findById(Integer priorityId) {
        return priorityRepository.findById(priorityId).orElseThrow(() -> new EntityException("Priority not found"));
    }
}
