package co.ke.finsis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.ke.finsis.payload.StationDto;
import co.ke.finsis.service.StationService;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    @PostMapping
    public ResponseEntity<StationDto> create(@RequestBody StationDto dto) {
        return ResponseEntity.ok(stationService.createStation(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StationDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(stationService.getStation(id));
    }

    @GetMapping
    public ResponseEntity<List<StationDto>> getAll() {
        return ResponseEntity.ok(stationService.getAllStations());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StationDto> update(@PathVariable Long id, @RequestBody StationDto dto) {
        return ResponseEntity.ok(stationService.updateStation(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stationService.deleteStation(id);
        return ResponseEntity.noContent().build();
    }
}
