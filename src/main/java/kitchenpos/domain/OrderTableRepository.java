package kitchenpos.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    Optional<OrderTable> findByOrderTable(OrderTable orderTable);
}
