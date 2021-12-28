package order.repository;

import java.util.*;

import org.springframework.data.jpa.repository.*;

import order.domain.*;
import table.domain.*;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderTable(OrderTable orderTable);
}
