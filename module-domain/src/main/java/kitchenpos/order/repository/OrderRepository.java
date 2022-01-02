package kitchenpos.order.repository;

import java.util.*;

import org.springframework.data.jpa.repository.*;

import kitchenpos.order.domain.*;
import kitchenpos.table.domain.*;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderTable(OrderTable orderTable);
}
