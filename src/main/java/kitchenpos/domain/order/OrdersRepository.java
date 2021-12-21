package kitchenpos.domain.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.vo.OrderTableId;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Orders findByOrderTableId(OrderTableId orderTableId);

    List<Orders> findAllByOrderTableIdIn(List<OrderTableId> orderTableIds);

    boolean existsByOrderTableIdInAndOrderStatusNot(List<OrderTableId> id, OrderStatus orderStatus);
}
