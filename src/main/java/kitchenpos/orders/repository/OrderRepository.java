package kitchenpos.orders.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.orders.domain.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
