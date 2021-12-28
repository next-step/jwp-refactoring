package kitchenpos.tobe.orders.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderTableIdAndStatusNot(final Long orderTableId, final OrderStatus status);
}
