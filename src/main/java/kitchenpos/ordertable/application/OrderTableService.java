package kitchenpos.ordertable.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertablegroup.domain.OrderTableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderTableService {

    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderTableService(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        OrderTable orderTable = orderTableRepository.save(request.toEntity(null, request.getNumberOfGuests(), request.isEmpty()));
        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
            .map(OrderTableResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest request) {
        OrderTable savedOrderTable = findById(orderTableId);
        List<Order> orders = orderRepository.findAllByOrderTable(savedOrderTable);
        savedOrderTable.changeEmpty(request.isEmpty(), isIncludeNotCompleteStatus(orders));
        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest request) {
        final OrderTable savedOrderTable = findById(orderTableId);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return OrderTableResponse.of(savedOrderTable);
    }

    public OrderTable findById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new IllegalArgumentException("테이블이 등록되어 있어야 한다."));
    }

    public List<OrderTable> findAllByIdIn(List<Long> orderTableIds) {
        return orderTableRepository.findAllById(orderTableIds);
    }

    public void unGroup(OrderTableGroup orderTableGroup) {
        List<OrderTable> orderTableGroups = orderTableRepository.findAllByOrderTableGroup(orderTableGroup);

        for (OrderTable orderTable : orderTableGroups) {
            List<Order> orders = orderRepository.findAllByOrderTable(orderTable);
            orderTable.ungroupTable(isIncludeNotCompleteStatus(orders));
        }
    }

    private boolean isIncludeNotCompleteStatus(List<Order> orders) {
        return orders.stream()
            .anyMatch(Order::isNotCompleteStatus);
    }
}
