package kitchenpos.order.domain;

import kitchenpos.common.domain.Validator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator implements Validator<Order> {

    private final OrderTableRepository tableRepository;

    public OrderValidator(OrderTableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @Override
    public void validate(Order order) {
        OrderTable table = tableRepository.table(order.tableId());
        if (table.isEmpty()) {
            throw new IllegalArgumentException(
                String.format("주문을 하는 테이블(%s)은 비어있을 수 없습니다.", table));
        }
    }
}
