package kitchenposNew.table.application;

import kitchenposNew.table.dto.OrderTableResponse;
import kitchenposNew.order.domain.Order;
import kitchenposNew.table.domain.OrderTable;
import kitchenposNew.order.domain.OrderRepository;
import kitchenposNew.table.domain.OrderTableRepository;
import kitchenposNew.table.dto.OrderTableRequest;
import kitchenposNew.order.exception.NotFoundOrder;
import kitchenposNew.order.exception.NotFoundOrderTable;
import kitchenposNew.table.exception.NotChangeToEmptyThatCookingOrMealTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable persistOrderTable = orderTableRepository.save(orderTableRequest.toOrderTable());
        return OrderTableResponse.of(persistOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll().stream()
                .map(orderTable -> OrderTableResponse.of(orderTable))
                .collect(Collectors.toList());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId).orElseThrow(() -> new NotFoundOrderTable());
        final Order order = orderRepository.findByOrderTableId(orderTableId).orElseThrow(() -> new NotFoundOrder());
        if (order.isCookingOrMeal()) {
            throw new NotChangeToEmptyThatCookingOrMealTable();
        }
        savedOrderTable.changeToEmpty();
        return OrderTableResponse.of(savedOrderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId).orElseThrow(() -> new NotFoundOrderTable());
        orderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());
        return OrderTableResponse.of(orderTable);
    }
}
