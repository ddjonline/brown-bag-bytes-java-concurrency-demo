package poc.java.concurrency.api.persistence.repo;

import java.util.concurrent.CompletableFuture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import poc.java.concurrency.api.persistence.entity.FirstHalfEntity;
import poc.java.concurrency.api.persistence.entity.FirstHalfEntityProjection;

@Repository
public interface FirstHalfRepository extends JpaRepository<FirstHalfEntity, Long> {

    // FirstHalfEntityProjection findFirstHalfEntityProjectionByPro(String value);
    CompletableFuture<FirstHalfEntityProjection> findFirstHalfEntityProjectionByPro(String value);
}
