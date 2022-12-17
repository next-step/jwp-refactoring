package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {

    private static final List<OrderStatus> COULD_NOT_CHANGE_EMPTY_STATUSES = Arrays.asList(
            OrderStatus.MEAL, OrderStatus.COOKING);
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableService(OrderTableRepository orderTableRepository,
            OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTable create(final OrderTable orderTable) {
        return orderTableRepository.save(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        checkNullId(orderTableId);
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다"));
        checkExistsByOrderTableIdAndOrderStatusIn(savedOrderTable.getId());
        savedOrderTable.changeEmpty(true);
        return savedOrderTable;
    }

    private void checkExistsByOrderTableIdAndOrderStatusIn(Long id) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(id, COULD_NOT_CHANGE_EMPTY_STATUSES)) {
            throw new IllegalArgumentException("주문 상태는 " + COULD_NOT_CHANGE_EMPTY_STATUSES.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")) + "가 아니어야 합니다");
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        checkNullId(orderTableId);
        orderTable.checkValidGuestNumber();
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeNumberOfGuest(orderTable.getNumberOfGuests());
        return savedOrderTable;
    }

    private void checkNullId(Long orderTableId) {
        if (orderTableId == null) {
            throw new IllegalArgumentException("요청 주문 테이블 id는 null이 아니어야 합니다");
        }
    }

    public void group(List<OrderTable> orderTables, Long id) {
        orderTables.forEach(it -> it.updateGroup(id));
    }

    public void unGroupTables(List<OrderTable> orderTableList) {
        checkExistsByOrderTableIdInAndOrderStatusIn(
                orderTableList.stream()
                        .map(OrderTable::getId).collect(Collectors.toList()));
        orderTableList.forEach(OrderTable::unGroup);
    }

    private void checkExistsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIsList) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIsList,
                COULD_NOT_CHANGE_EMPTY_STATUSES)) {
            throw new IllegalArgumentException("주문 상태는 " + COULD_NOT_CHANGE_EMPTY_STATUSES.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")) + "가 아니어야 합니다");
        }
    }
}
