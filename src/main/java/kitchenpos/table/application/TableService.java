package kitchenpos.table.application;

import kitchenpos.order.domain.order.Order;
import kitchenpos.order.domain.order.OrderRepository;
import kitchenpos.order.domain.order.OrderStatus;
import kitchenpos.table.domain.table.OrderTable;
import kitchenpos.table.domain.table.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
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

    public OrderTableResponse saveOrderTable(final OrderTableRequest orderTableRequest) {
        return OrderTableResponse.of(orderTableRepository.save(orderTableRequest.toOrderTable()));
    }

    public List<OrderTableResponse> findAllOrderTable() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        checkOrderStatus(savedOrderTable);
        savedOrderTable.changeEmpty();

        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }

    private void checkOrderStatus(OrderTable savedOrderTable) {
        final List<Order> findOrders = orderRepository.findAllByOrderTable(savedOrderTable);
        boolean isCompleted = findOrders.stream()
                .allMatch(it -> it.getOrderStatus().equals(OrderStatus.COMPLETION));
        if (!isCompleted) {
            throw new IllegalArgumentException("주문 완료가 되지 않았습니다.");
        }
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

        savedOrderTable.changeNumberOfGuests(orderTableRequest.getNumberOfGuests());

        return OrderTableResponse.of(orderTableRepository.save(savedOrderTable));
    }
}
