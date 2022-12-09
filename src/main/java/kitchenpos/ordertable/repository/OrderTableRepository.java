package kitchenpos.ordertable.repository;

import kitchenpos.menu.domain.Menu;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

}
