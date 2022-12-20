package kitchenpos.order.application;

import kitchenpos.exception.ErrorMessage;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTableResponse create(OrderTableRequest request) {
        return OrderTableResponse.of(orderTableRepository.save(request.toOrderTable()));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return OrderTableResponse.list(orderTableRepository.findAll());
    }

    public OrderTableResponse changeEmpty(Long orderTableId, OrderEmpty request) {
        OrderTable savedOrderTable = findOrderTableById(orderTableId);
        List<Order> orders = findAllByOrderTableId(savedOrderTable.getId());

        savedOrderTable.changeEmpty(request.isEmpty(), orders);

        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.ORDER_TABLE_NOT_FOUND_BY_ID.getMessage()));
    }

    private List<Order> findAllByOrderTableId(Long orderTableId) {
        return orderRepository.findAllByOrderTableId(orderTableId);
    }

    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderGuests request) {
        OrderTable orderTable = findOrderTableById(orderTableId);
        orderTable.changeNumberOfGuests(request.getNumberOfGuests());

        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }
}
