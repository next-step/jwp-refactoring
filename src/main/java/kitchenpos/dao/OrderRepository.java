package kitchenpos.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
