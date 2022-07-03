package kitchenpos.table.dao;

import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
}
