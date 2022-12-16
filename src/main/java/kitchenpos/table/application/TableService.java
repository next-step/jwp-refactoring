package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Transactional(readOnly = true)
@Service
public class TableService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableService(OrderRepository orderRepository, OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTableResponse create(OrderTableRequest orderTableRequest) {
        OrderTable savedOrderTable = orderTableRepository.save(orderTableRequest.toEntity());
        return OrderTableResponse.from(savedOrderTable);
    }

    public List<OrderTableResponse> list() {
        List<OrderTable> orderTables = orderTableRepository.findAll();
        return OrderTableResponse.toList(orderTables);
    }

    @Transactional
    public OrderTableResponse changeEmpty(Long orderTableId, OrderTableRequest request) {
        OrderTable savedOrderTable = findOrderTable(orderTableId);

        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException("테이블 그룹이 등록되어 있다면 테이블의 상태를 변경할 수 없다.");
        }

        if (orderRepository.existsByOrderTableAndOrderStatusIn(savedOrderTable, OrderStatus.notCompletion())) {
            throw new IllegalArgumentException("주문 테이블의 상태가 조리 또는 식사일 경우 테이블의 상태를 변경할 수 없다.");
        }

        savedOrderTable.changeEmptyStatus(request.isEmpty());

        OrderTable changedOrderTable = orderTableRepository.save(savedOrderTable);
        return OrderTableResponse.from(changedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(Long orderTableId, OrderTableRequest request) {
        OrderTable savedOrderTable = findOrderTable(orderTableId);

        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());

        OrderTable changedOrderTable = orderTableRepository.save(savedOrderTable);
        return OrderTableResponse.from(changedOrderTable);
    }

    private OrderTable findOrderTable(Long orderTableId) {
        OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다."));
        return savedOrderTable;
    }
}
