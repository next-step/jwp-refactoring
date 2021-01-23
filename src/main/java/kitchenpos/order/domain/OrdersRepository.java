package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.ordertable.domain.OrderTable;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
	Orders findByOrderTable(OrderTable orderTable);
}
