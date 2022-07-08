package kitchenpos.order.validator;

import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.exception.TableException;
import kitchenpos.table.exception.TableExceptionType;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(final OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(final OrderRequest orderRequest) {
        final OrderTable orderTable = findByOrderTableId(orderRequest);
        orderTable.validateUsed();
    }

    private OrderTable findByOrderTableId(final OrderRequest orderRequest) {
        return orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new TableException(TableExceptionType.TABLE_NOT_FOUND));
    }
}
