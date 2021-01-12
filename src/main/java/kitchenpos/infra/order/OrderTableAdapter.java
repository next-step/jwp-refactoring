package kitchenpos.infra.order;

import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.OrderTableRepository;
import kitchenpos.domain.order.SafeOrderTable;
import kitchenpos.domain.order.exceptions.InvalidTryOrderException;
import org.springframework.stereotype.Component;

@Component
public class OrderTableAdapter implements SafeOrderTable {
    private final OrderTableRepository orderTableRepository;

    public OrderTableAdapter(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public void canOrderAtThisTable(final Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new InvalidTryOrderException("존재하지 않는 주문 테이블에서 주문할 수 없습니다."));

        if (orderTable.isEmpty()) {
            throw new InvalidTryOrderException("비어있는 주문 테이블에서 주문할 수 없습니다.");
        }
    }
}
