package kitchenpos.infra.order;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.exceptions.orderTable.OrderTableEntityNotFoundException;
import kitchenpos.domain.order.SafeOrderTable;
import kitchenpos.domain.order.exceptions.InvalidTryOrderException;
import org.springframework.stereotype.Component;

@Component
public class OrderTableAdapter implements SafeOrderTable {
    private final OrderTableDao orderTableDao;

    public OrderTableAdapter(final OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    @Override
    public void canOrderAtThisTable(final Long orderTableId) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new OrderTableEntityNotFoundException("존재하지 않는 주문 테이블에서 주문할 수 없습니다."));

        if (orderTable.isEmpty()) {
            throw new InvalidTryOrderException("비어있는 주문 테이블에서 주문할 수 없습니다.");
        }
    }
}
