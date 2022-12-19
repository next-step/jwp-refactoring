package kitchenpos.ordertable.application;

import java.util.List;
import java.util.stream.Collectors;
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
    public OrderTableResponse changeEmpty(final Long orderTableId, final UpdateEmptyRequest request) {
        final OrderTable savedOrderTable = findOrderTableById(orderTableId);
        List<Order> orders = findAllOrderByOrderTableId(orderTableId);

        savedOrderTable.updateEmpty(request.isEmpty(), orders);
        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
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

        return OrderTableResponse.from(orderTableRepository.save(savedOrderTable));
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        return savedOrderTable;
    }
}
