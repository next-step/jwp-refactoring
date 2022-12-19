package kitchenpos.order.dao;

import kitchenpos.order.domain.Order;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderTableId(Long orderTableId);
}
