package co.ke.finsis.service;

import co.ke.finsis.entity.Group;
import co.ke.finsis.payload.GroupDTO;
import co.ke.finsis.repository.GroupRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupDTO createGroup(GroupDTO dto) {
        Group group = mapToEntity(dto);
        return mapToDTO(groupRepository.save(group));
    }

    public GroupDTO getGroupById(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));
        return mapToDTO(group);
    }

    public List<GroupDTO> getAllGroups() {
        return groupRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public GroupDTO updateGroup(Long id, GroupDTO dto) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));

        group.setGroupName(dto.getGroupName());
        group.setCounty(dto.getCounty());
        group.setSubCounty(dto.getSubCounty());
        group.setWard(dto.getWard());
        group.setVillage(dto.getVillage());
        group.setNearestLandmark(dto.getNearestLandmark());
        group.setOfficeType(dto.getOfficeType());

        return mapToDTO(groupRepository.save(group));
    }

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }

    private GroupDTO mapToDTO(Group group) {
        return GroupDTO.builder()
                .id(group.getId())
                .groupName(group.getGroupName())
                .county(group.getCounty())
                .subCounty(group.getSubCounty())
                .ward(group.getWard())
                .village(group.getVillage())
                .nearestLandmark(group.getNearestLandmark())
                .officeType(group.getOfficeType())
                .build();
    }

    private Group mapToEntity(GroupDTO dto) {
        return Group.builder()
                .groupName(dto.getGroupName())
                .county(dto.getCounty())
                .subCounty(dto.getSubCounty())
                .ward(dto.getWard())
                .village(dto.getVillage())
                .nearestLandmark(dto.getNearestLandmark())
                .officeType(dto.getOfficeType())
                .build();
    }

    public Optional<GroupDTO> getGroupByName(String groupName) {
        return groupRepository.findByGroupName(groupName)
                .map(this::mapToDTO);
    }

    public Optional<GroupDTO> updateGroupByName(String groupName, GroupDTO dto) {
        return groupRepository.findByGroupName(groupName)
                .map(group -> {
                    group.setCounty(dto.getCounty());
                    group.setSubCounty(dto.getSubCounty());
                    group.setWard(dto.getWard());
                    group.setVillage(dto.getVillage());
                    group.setNearestLandmark(dto.getNearestLandmark());
                    group.setOfficeType(dto.getOfficeType());
                    return mapToDTO(groupRepository.save(group));
                });
    }

    public boolean deleteGroupByName(String groupName) {
        return groupRepository.findByGroupName(groupName)
                .map(group -> {
                    groupRepository.delete(group);
                    return true;
                }).orElse(false);
    }
}
