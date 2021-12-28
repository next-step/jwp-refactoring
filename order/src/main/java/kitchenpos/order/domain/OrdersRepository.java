package kitchenpos.order.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Orders findByOrderTableId(OrderTableId orderTableId);

    List<Orders> findAllByOrderTableIdIn(List<OrderTableId> orderTableIds);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<OrderTableId> id, List<OrderStatus> orderStatus);
}
