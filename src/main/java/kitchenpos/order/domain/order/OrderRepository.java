package kitchenpos.order.domain.order;

import kitchenpos.table.domain.table.OrderTable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Override
    @EntityGraph(attributePaths = {"orderLineItems.orderLineItems", "orderTable"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Order> findAll();

    @Override
    @EntityGraph(attributePaths = "orderTable", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Order> findById(Long aLong);

    List<Order> findAllByOrderTable(OrderTable orderTable);
}
