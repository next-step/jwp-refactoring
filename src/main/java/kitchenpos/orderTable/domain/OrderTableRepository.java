package kitchenpos.orderTable.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);

    Optional<OrderTable> findByIdAndEmptyIs(Long id, boolean empty);
}
