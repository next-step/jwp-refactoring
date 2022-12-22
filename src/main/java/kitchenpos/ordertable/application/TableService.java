package kitchenpos.ordertable.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import kitchenpos.common.error.ErrorEnum;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.dto.UpdateEmptyRequest;
import kitchenpos.ordertable.dto.UpdateNumberOfGuestsRequest;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        final OrderTable orderTable = orderTableRepository.save(request.createOrderTable());
        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTableResponse> findAll() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final UpdateEmptyRequest request) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        final List<Order> orders = findAllOrderByOrderTableId(orderTableId);

        validateOngoingOrder(orders);
        savedOrderTable.updateEmpty(request.isEmpty());
        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }

    private void validateOngoingOrder(List<Order> orders) {
        orders.forEach(Order::validateOrderStatusShouldComplete);
    }

    private List<Order> findAllOrderByOrderTableId(Long id) {
        return orderRepository.findAllByOrderTableId(id);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(
            final Long orderTableId,
            final UpdateNumberOfGuestsRequest request
    ) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        savedOrderTable.updateNumberOfGuest(request.getNumberOfGuests());

        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorEnum.ORDER_TABLE_NOT_FOUND.message()));
        return savedOrderTable;
    }
}
