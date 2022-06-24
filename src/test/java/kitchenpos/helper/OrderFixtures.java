package kitchenpos.helper;

import java.time.LocalDateTime;
import kitchenpos.domain.OrderStatus;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;

public class OrderFixtures {
    public static Order 주문_만들기(OrderStatus orderStatus){
        return 주문_만들기(null, orderStatus, null);
    }
    public static Order 주문_만들기(Long id, OrderStatus orderStatus, OrderTable orderTable){
        return new Order(id, orderStatus, LocalDateTime.now(), orderTable);
    }
}
