package kitchenpos.table.application;

import java.util.stream.Collectors;
import kitchenpos.core.exception.BadRequestException;
import kitchenpos.core.exception.ExceptionType;
import kitchenpos.core.exception.NotFoundException;
import kitchenpos.order.application.OrderTableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableService orderTableService;

    public TableService(OrderTableRepository orderTableRepository, OrderTableService orderTableService) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableService = orderTableService;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = OrderTable.of(null, orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());
        orderTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.of(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTableEntities = orderTableRepository.findAll();
        return orderTableEntities.stream()
            .map(OrderTableResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new NotFoundException(ExceptionType.NOT_EXIST_ORDER_TABLE));
        orderTable.validateHasTableGroupId();

        orderTableService.validateOrderTableStatus(orderTable);

        orderTable.emptyTheTable();
        orderTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.of(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final int numberOfGuests = orderTableRequest.getNumberOfGuests();
        validateNumberOfGuests(numberOfGuests);

        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new NotFoundException(ExceptionType.NOT_EXIST_ORDER_TABLE));
        savedOrderTable.validateIsEmpty();

        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new BadRequestException(ExceptionType.CAN_NOT_LESS_THAN_ZERO_GUESTS);
        }
    }
}
