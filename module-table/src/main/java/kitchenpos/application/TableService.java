package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.exception.TableException;
import kitchenpos.exception.TableExceptionType;
import kitchenpos.validator.OrderStatusValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderStatusValidator orderStatusValidator;

    public TableService(final OrderTableRepository orderTableRepository, final OrderStatusValidator orderStatusValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderStatusValidator = orderStatusValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTable orderTable) {
        final OrderTable save = orderTableRepository.save(orderTable);
        return OrderTableResponse.of(save);
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.ofList(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        orderStatusValidator.validateEnabledClear(orderTableId);
        final OrderTable savedOrderTable = findById(orderTableId);
        savedOrderTable.updateEmptyTable(orderTable);

        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = findById(orderTableId);
        savedOrderTable.changeNumberOfGuest(orderTable);

        return OrderTableResponse.of(savedOrderTable);
    }

    private OrderTable findById(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new TableException(TableExceptionType.TABLE_NOT_FOUND));
    }
}
