package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.table.domain.OrderTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query(value = "select o from OrderEntity o "
        + "join fetch o.orderLineItems")
    List<OrderEntity> findAllOrderAndItems();

    @Query(value = "select o from OrderEntity o "
        + "join fetch o.orderLineItems oli "
        + "where o = :order")
    OrderEntity findAllOrderAndItemsByOrder(OrderEntity order);

    boolean existsByOrderTableAndOrderStatusIn(OrderTableEntity orderTable, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableInAndOrderStatusIn(List<OrderTableEntity> orderTableEntities, List<OrderStatus> orderStatuses);
}
