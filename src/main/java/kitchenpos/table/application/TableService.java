package kitchenpos.table.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableService(final OrderTableRepository orderTableRepository,
                        final OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest request) {
        final OrderTable persistOrderTable = orderTableRepository.save(request.toOrderTable());
        return OrderTableResponse.of(persistOrderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableRepository.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTable changeEmpty(final Long id, final OrderTableRequest request) {
        final OrderTable savedOrderTable = findByIdElseThrow(id);
        checkIfPossibleToChangeEmpty(savedOrderTable);
        savedOrderTable.changeEmpty(request.isEmpty());
        return orderTableRepository.save(savedOrderTable);
    }

    private OrderTable findByIdElseThrow(final Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("주문 테이블을 찾을 수 없습니다. id: %d", id)));
    }

    private void checkIfPossibleToChangeEmpty(final OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalStateException("그룹이 지정된 테이블의 비었는지 여부를 변경할 수 없습니다.");
        }
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), OrderStatus.notCompletes())) {
            throw new IllegalStateException("주문이 완료되지 않은 테이블의 비었는지 여부를 변경할 수 없습니다.");
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long id, final OrderTableRequest request) {
        final OrderTable savedOrderTable = getById(id);
        checkIfPossibleToChangeNumberOfGuests(savedOrderTable);
        savedOrderTable.changeNumberOfGuests(request.getNumberOfGuests());
        return orderTableRepository.save(savedOrderTable);
    }

    private void checkIfPossibleToChangeNumberOfGuests(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new IllegalStateException("빈 테이블의 손님 수는 변경할 수 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public OrderTable getById(final Long id) {
        return findByIdElseThrow(id);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> getAllByTableGroupId(final Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }
}
