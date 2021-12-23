package kichenpos.order.order.domain;

import kichenpos.common.domain.Validator;
import kichenpos.order.table.infrastructure.OrderTableClient;
import kichenpos.order.table.infrastructure.dto.OrderTableDto;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator implements Validator<Order> {

    private final OrderTableClient tableClient;

    public OrderValidator(OrderTableClient tableClient) {
        this.tableClient = tableClient;
    }

    @Override
    public void validate(Order order) {
        OrderTableDto table = tableClient.getTable(order.tableId());
        if (table.isEmpty()) {
            throw new IllegalArgumentException(
                String.format("주문을 하는 테이블(%s)은 비어있을 수 없습니다.", table));
        }
    }
}
