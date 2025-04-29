package org.itltcanz.tms.service;

import lombok.RequiredArgsConstructor;
import org.itltcanz.tms.dto.desk.DeskInDto;
import org.itltcanz.tms.dto.desk.DeskOutDto;
import org.itltcanz.tms.entity.BoardEntity;
import org.itltcanz.tms.exceptions.EntityException;
import org.itltcanz.tms.repository.DeskRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final DeskRepository deskRepository;
    private final UserService userService;

    private final ModelMapper modelMapper;

    // Controllers methods

    public DeskOutDto save(DeskInDto deskInDto) {
        var currentUser = userService.getCurrentUser();
        var unsavedDeskEntity = modelMapper.map(deskInDto, BoardEntity.class);
        unsavedDeskEntity.getUsers().add(currentUser);
        var savedDeskEntity = deskRepository.save(unsavedDeskEntity);
        return modelMapper.map(savedDeskEntity, DeskOutDto.class);
    }

    public Page<DeskOutDto> getDesks(PageRequest pageable) {
        var currentUser = userService.getCurrentUser();
        Page<BoardEntity> deskEntities;
        if (userService.isAdmin(currentUser)) {
            deskEntities = deskRepository.findAll(pageable);
        } else {
            deskEntities = deskRepository.findDesksByUsersId(currentUser.getId(), pageable);
        }
        return deskEntities.map(boardEntity -> modelMapper.map(boardEntity, DeskOutDto.class));
    }

    public DeskOutDto getDeskById(Integer deskId, boolean includeUsers, boolean includeStatuses) {
        var currentUser = userService.getCurrentUser();
        var deskEntity = findByIdOrThrow(deskId);
        if (userService.isAdmin(currentUser) || deskEntity.getUsers().contains(currentUser)) {
            return modelMapper.map(deskEntity, DeskOutDto.class);
        } else {
            throw new AccessDeniedException("You do not have permission to access this desk");
        }
    }

    // Internal methods

    public BoardEntity findByIdOrThrow(Integer id) {
        return deskRepository.findById(id).orElseThrow(() -> new EntityException("Desk not found"));
    }
}
