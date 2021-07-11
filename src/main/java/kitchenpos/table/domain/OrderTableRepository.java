package kitchenpos.table.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    Optional<OrderTable> findByIdAndEmptyIsFalse(long id);
}
