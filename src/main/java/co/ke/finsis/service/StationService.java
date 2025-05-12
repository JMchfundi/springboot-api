package co.ke.finsis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import co.ke.finsis.entity.Station;
import co.ke.finsis.payload.StationDto;
import co.ke.finsis.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StationService {

    private final StationRepository stationRepository;

    // Utility methods for mapping
    private StationDto mapToDto(Station station) {
        return StationDto.builder()
                .id(station.getId())
                .stationName(station.getStationName())
                .stationCode(station.getStationCode())
                .county(station.getCounty())
                .subCounty(station.getSubCounty())
                .ward(station.getWard())
                .gps(station.getGps())
                .economicActivities(station.getEconomicActivities())
                .electricity(station.getElectricity())
                .networkProviders(station.getNetworkProviders())
                .internet(station.getInternet())
                .staffCount(station.getStaffCount())
                .roadAccess(station.getRoadAccess())
                .licenseType(station.getLicenseType())
                .regAuthority(station.getRegAuthority())
                .mobileMoney(station.getMobileMoney())
                .services(station.getServices())
                .build();
    }

    private Station mapToEntity(StationDto dto) {
        return Station.builder()
                .id(dto.getId())
                .stationName(dto.getStationName())
                .stationCode(dto.getStationCode())
                .county(dto.getCounty())
                .subCounty(dto.getSubCounty())
                .ward(dto.getWard())
                .gps(dto.getGps())
                .economicActivities(dto.getEconomicActivities())
                .electricity(dto.getElectricity())
                .networkProviders(dto.getNetworkProviders())
                .internet(dto.getInternet())
                .staffCount(dto.getStaffCount())
                .roadAccess(dto.getRoadAccess())
                .licenseType(dto.getLicenseType())
                .regAuthority(dto.getRegAuthority())
                .mobileMoney(dto.getMobileMoney())
                .services(dto.getServices())
                .build();
    }

    // CRUD methods

    public StationDto createStation(StationDto dto) {
        Station station = mapToEntity(dto);
        return mapToDto(stationRepository.save(station));
    }

    public StationDto getStation(Long id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Station not found"));
        return mapToDto(station);
    }

    public List<StationDto> getAllStations() {
        return stationRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public StationDto updateStation(Long id, StationDto dto) {
        Station existing = stationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Station not found"));
        Station updated = mapToEntity(dto);
        updated.setId(id);
        return mapToDto(stationRepository.save(updated));
    }

    public void deleteStation(Long id) {
        stationRepository.deleteById(id);
    }
}
