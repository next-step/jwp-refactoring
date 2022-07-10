package kitchenpos.validator;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.OrderRequest;
import kitchenpos.exception.TableException;
import kitchenpos.exception.TableExceptionType;
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
