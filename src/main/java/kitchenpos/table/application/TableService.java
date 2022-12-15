package kitchenpos.table.application;

import kitchenpos.ExceptionMessage;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        final NumberOfGuests guestNumber = new NumberOfGuests(orderTableRequest.getNumberOfGuests());
        final OrderTable orderTable = new OrderTable(guestNumber, orderTableRequest.getEmpty());
        final OrderTable savedOrderTable = orderTableRepository.save(orderTable);
        return OrderTableResponse.of(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    public OrderTable findById (Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NOT_EXIST_ORDER_TABLE.getMessage()));
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, boolean empty) {
        final OrderTable savedOrderTable = findById(orderTableId);
        final Order order = findOrderByOrderTableId(orderTableId);
        order.checkCookingOrMeal();
        savedOrderTable.changeEmpty(empty);
        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, int numberOfGuests) {
        final OrderTable savedOrderTable = findById(orderTableId);
        final NumberOfGuests newNumberOfGuests = new NumberOfGuests(numberOfGuests);
        savedOrderTable.changeNumberOfGuests(newNumberOfGuests);
        return OrderTableResponse.of(savedOrderTable);
    }

    public Order findOrderByOrderTableId(Long orderTableId) {
        OrderTable orderTable = findById(orderTableId);
        return orderRepository.findOrderByOrderTable(orderTable)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NOT_EXIST_ORDER_TABLE.getMessage()));
    }
    public List<Order> findOrderByOrderTableIds(List<Long> orderTableIds) {
        return orderTableIds.stream()
                .map(this::findOrderByOrderTableId)
                .collect(Collectors.toList());
    }
}
