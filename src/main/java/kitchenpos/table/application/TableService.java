package kitchenpos.table.application;

import static kitchenpos.order.domain.OrderStatus.STARTED_ORDER_READY_STATUS;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository,
        OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = orderTableRequest.toOrderTable();
        orderTable.ungroup();

        orderTableRepository.save(orderTable);

        return OrderTableResponse.of(orderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return orderTables.stream()
            .map(OrderTableResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = this.findById(orderTableId);
        validateExistStartedOrderReadyStatus(orderTableId);

        orderTable.changeEmpty(orderTableRequest.isEmpty());

        return OrderTableResponse.of(orderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableRequest orderTableRequest) {
        final OrderTable orderTable = this.findById(orderTableId);
        orderTable.updateNumberOfGuests(orderTableRequest.getNumberOfGuests());

        return OrderTableResponse.of(orderTable);
    }

    private OrderTable findById(Long id) {
        return this.orderTableRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다."));
    }

    private void validateExistStartedOrderReadyStatus(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, STARTED_ORDER_READY_STATUS)) {
            throw new IllegalArgumentException("이미 주문 준비를 시작한 테이블이 있습니다.");
        }
    }

}
