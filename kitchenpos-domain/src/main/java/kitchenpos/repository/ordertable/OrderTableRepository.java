package kitchenpos.repository.ordertable;

import kitchenpos.domain.ordertable.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
}
