package kichenpos.order.infra;

import kichenpos.order.domain.Order;
import kichenpos.order.domain.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderRepository extends JpaRepository<Order, Long>, OrderRepository {
}
