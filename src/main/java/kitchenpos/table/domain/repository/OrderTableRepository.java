package kitchenpos.table.domain.repository;

import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
}
