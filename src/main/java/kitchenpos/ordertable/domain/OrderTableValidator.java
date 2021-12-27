package kitchenpos.ordertable.domain;

import kitchenpos.order.exception.ClosedTableOrderException;
import kitchenpos.ordertable.exception.TableNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {

    private final OrderTableRepository orderTableRepository;

    public OrderTableValidator(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validateNotOrderClosedTable(Long tableId) {
        OrderTable orderTable = orderTableRepository.findById(tableId)
            .orElseThrow(TableNotFoundException::new);
        if (orderTable.isOrderClose()) {
            throw new ClosedTableOrderException();
        }
    }
}
