package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderFixture {
    private OrderFixture() {
    }

    public static Order orderParam(Long orderTableId, List<OrderLineItem> orderLineItems) {
        return new Order(null, orderTableId, null, null, orderLineItems);

    }

    public static Order orderParam(String orderStatus) {
        return new Order(null, null, orderStatus, null, null);
    }

    public static Order savedOrder(Long id, String orderStatus) {
        return new Order(id, 1L, orderStatus, LocalDateTime.now(), new ArrayList<>());
    }

    public static Order savedOrder(Long id, Long orderTableId) {
        return new Order(id, orderTableId, "COOKING", LocalDateTime.now(), new ArrayList<>());
    }
}
