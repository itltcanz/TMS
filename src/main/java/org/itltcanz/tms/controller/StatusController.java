package org.itltcanz.tms.controller;

import lombok.RequiredArgsConstructor;
import org.itltcanz.tms.dto.status.StatusNameDto;
import org.itltcanz.tms.service.StatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/desks/{deskId}/statuses")
@RequiredArgsConstructor
public class StatusController {
    private final StatusService statusService;

    @PostMapping
    public ResponseEntity<StatusNameDto> createStatus(@PathVariable("{deskId}") Integer deskId) {
        return null;
    }

}
