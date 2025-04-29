package org.itltcanz.tms.controller;

import lombok.RequiredArgsConstructor;
import org.itltcanz.tms.dto.desk.DeskInDto;
import org.itltcanz.tms.dto.desk.DeskOutDto;
import org.itltcanz.tms.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/desks")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DeskOutDto> createDesk(@RequestParam DeskInDto deskInDto) {
        var deskOutDto = boardService.save(deskInDto);
        return ResponseEntity.ok(deskOutDto);
    }

    @GetMapping
    public ResponseEntity<Page<DeskOutDto>> getDesks(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        var pageable = PageRequest.of(page, size);
        var deskOutDtos = boardService.getDesks(pageable);
        return ResponseEntity.ok(deskOutDtos);
    }

    @GetMapping("/{deskId}")
    public ResponseEntity<DeskOutDto> getDeskById(
        @PathVariable Integer deskId,
        @RequestParam(defaultValue = "false") boolean includeUsers,
        @RequestParam(defaultValue = "false") boolean includeStatuses
    ) {
        var deskOutDto = boardService.getDeskById(deskId, includeUsers, includeStatuses);
        return ResponseEntity.ok(deskOutDto);
    }
}
