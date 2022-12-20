package kitchenpos.table.ordertable.application;

import kitchenpos.orderstatus.domain.Status;
import kitchenpos.orderstatus.repository.OrderStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kitchenpos.table.ordertable.domain.OrderTable;
import kitchenpos.table.ordertable.domain.OrderTableRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {

    private static final List<Status> COULD_NOT_CHANGE_EMPTY_STATUSES = Arrays.asList(Status.MEAL, Status.COOKING);
    private final OrderTableRepository orderTableRepository;
    private final OrderStatusRepository orderStatusRepository;

    public TableService(OrderTableRepository orderTableRepository,
            OrderStatusRepository orderStatusRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderStatusRepository = orderStatusRepository;
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
        if (orderStatusRepository.existsByOrderTableIdAndStatusIn(id, COULD_NOT_CHANGE_EMPTY_STATUSES)) {
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
}
