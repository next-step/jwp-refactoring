package kitchenpos.order.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;

@Repository(value = "ToBeJpaOrderRepository")
public interface JpaOrderRepository extends OrderRepository, JpaRepository<Order, Long> {
}
