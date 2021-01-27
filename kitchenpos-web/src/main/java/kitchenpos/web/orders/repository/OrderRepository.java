package kitchenpos.web.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.orders.domain.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
