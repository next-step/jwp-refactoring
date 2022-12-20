package kitchenpos.ordertable.application;

import java.util.List;
import kitchenpos.constants.ErrorMessages;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTableValidator;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderTableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public OrderTableService(final OrderTableRepository orderTableRepository,
            final OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRequest.createOrderTable();
        return OrderTableResponse.from(orderTableRepository.save(orderTable));
    }

    public List<OrderTableResponse> list() {
        return OrderTableResponse.getMenuGroupResponses(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = getSavedOrderTable(orderTableId);
        orderTableValidator.validateEmptyChangable(savedOrderTable);
        savedOrderTable.updateEmpty(orderTableRequest.isEmpty());
        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = getSavedOrderTable(orderTableId);
        savedOrderTable.updateNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    private OrderTable getSavedOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.ORDER_TABLE_DOES_NOT_EXIST));
    }
}
