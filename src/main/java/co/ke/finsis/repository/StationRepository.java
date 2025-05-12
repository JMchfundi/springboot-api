package co.ke.finsis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.ke.finsis.entity.Station;

public interface StationRepository extends JpaRepository<Station, Long> {
}
