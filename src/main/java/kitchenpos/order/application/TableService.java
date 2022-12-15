package kitchenpos.order.application;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.order.dto.UpdateEmptyRequest;
import kitchenpos.order.dto.UpdateNumberOfGuestsRequest;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.order.validator.OrderTableValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableService(OrderTableRepository orderTableRepository, OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @Transactional
    public OrderTableResponse create(OrderTableRequest request) {
        OrderTable orderTable = orderTableRepository.save(request.createOrderTable());
        return OrderTableResponse.from(orderTable);
    }

    public List<OrderTableResponse> findAll() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse updateEmpty(Long orderTableId, UpdateEmptyRequest request) {
        OrderTable savedOrderTable = findOrderTableById(orderTableId);
        orderTableValidator.validateUpdateEmpty(savedOrderTable);

        savedOrderTable.setEmpty(request.isEmpty());
        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    @Transactional
    public OrderTableResponse updateNumberOfGuests(
            Long orderTableId,
            UpdateNumberOfGuestsRequest request
    ) {
        OrderTable savedOrderTable = findOrderTableById(orderTableId);
        orderTableValidator.validateUpdateNumberOfGuests(savedOrderTable);

        savedOrderTable.setNumberOfGuest(request.getNumberOfGuests());
        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.ORDER_TABLE_IS_NOT_EXIST.getMessage()));
    }
}
