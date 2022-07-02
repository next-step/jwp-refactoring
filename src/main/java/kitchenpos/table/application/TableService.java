package kitchenpos.table.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.NumberOfGuestsRequest;
import kitchenpos.table.dto.OrderTableEmptyRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderService orderService;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderService orderService, final OrderTableRepository orderTableRepository) {
        this.orderService = orderService;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = orderTableRepository.save(OrderTable.of(request));
        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableEmptyRequest request) {
        final OrderTable savedOrderTable = findByIdOrElseThrow(orderTableId);

        savedOrderTable.validateGroupTable();
        validateOrderTableIdAndOrderStatusIn(orderTableId);

        savedOrderTable.changeEmpty(request.isEmpty());

        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final NumberOfGuestsRequest request) {
        final OrderTable persistOrderTable = orderService.findOrderTableById(orderTableId);

        persistOrderTable.validateOrderTableEmpty();
        persistOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.of(persistOrderTable);
    }

    private void validateOrderTableIdAndOrderStatusIn(Long orderTableId) {
        if (orderService.existsOrderByOrderTableIdAndOrderStatusIn(orderTableId, OrderStatus.getCookingAndMeal())) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable findByIdOrElseThrow(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
