package kitchenpos.table.application;

import java.util.Arrays;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.infrastructure.OrderRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.infrastructure.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableService(final OrderTableRepository orderTableRepository,
                        OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = OrderTable.of(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = findUnGroupedTable(orderTableId);
        validateOrderStatus(orderTableId);
        orderTable.updateEmpty(orderTableRequest.toEntity());
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    private void validateOrderStatus(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("조리 및 식사 중일때는 변경할 수 없습니다.");
        }
    }

    private OrderTable findUnGroupedTable(Long orderTableId) {
        OrderTable orderTable = findOrderTable(orderTableId);

        if (orderTable.hasGroup()) {
            throw new IllegalArgumentException("이미 단체 지정된 테이블이 존재합니다.");
        }
        return orderTable;
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = findOrderTable(orderTableId);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 손님수를 수정할 수 없습니다.");
        }

        orderTable.updateNumberOfGuests(orderTableRequest.toEntity());
        return OrderTableResponse.of(orderTableRepository.save(orderTable));
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));
    }
}
