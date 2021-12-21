package kitchenpos.dao;

import kitchenpos.domain.order.OrderTable;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableDao extends JpaRepository<OrderTable, Long> {

}
