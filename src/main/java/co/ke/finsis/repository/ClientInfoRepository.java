package co.ke.finsis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.ke.finsis.entity.ClientInfo;

public interface ClientInfoRepository extends JpaRepository<ClientInfo, Long> {
    // You can add custom query methods here if needed
}
