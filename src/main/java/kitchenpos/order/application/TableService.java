package kitchenpos.order.application;

import kitchenpos.exception.OrderTableError;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(OrderTableRequest request) {
        return OrderTableResponse.of(orderTableRepository.save(request.toOrderTable()));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return OrderTableResponse.list(orderTableRepository.findAll());
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableRequest request) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new EntityNotFoundException(OrderTableError.NOT_FOUND));
        List<Order> orders = orderRepository.findAllByOrderTableId(savedOrderTable.getId());

        validateOngoingOrder(orders);
        savedOrderTable.changeEmpty(request.isEmpty());

        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }

    private void validateOngoingOrder(List<Order> orders) {
        orders.forEach(Order::checkOngoingOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTableRequest request) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new EntityNotFoundException(OrderTableError.NOT_FOUND));
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }
}
