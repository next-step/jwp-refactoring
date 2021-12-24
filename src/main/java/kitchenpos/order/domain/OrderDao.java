package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<Order, Long> {
}
