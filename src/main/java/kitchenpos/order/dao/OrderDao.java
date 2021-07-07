package kitchenpos.order.dao;

import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<Order, Long> {

}
