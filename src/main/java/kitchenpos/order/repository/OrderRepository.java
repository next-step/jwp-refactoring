package kitchenpos.order.repository;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
