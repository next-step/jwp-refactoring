package kitchenpos.domain.order;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderTable(OrderTable orderTable);

    List<Order> findAllByOrderTableIn(List<OrderTable> orderTables);
}
