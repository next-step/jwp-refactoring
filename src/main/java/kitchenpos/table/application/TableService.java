package kitchenpos.table.application;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

@Service
public class TableService {

    private static final List<OrderStatus> COULD_NOT_CHANGE_EMPTY_STATUSES = Arrays.asList(
            OrderStatus.MEAL, OrderStatus.COOKING);
    private final OrderTableRepository orderTableRepository;
    private final OrderService orderService;

    public TableService(OrderTableRepository orderTableRepository, OrderService orderService) {
        this.orderTableRepository = orderTableRepository;
        this.orderService = orderService;
    }

    @Transactional(isolation = READ_COMMITTED)
    public OrderTable create(final OrderTable orderTable) {
        return orderTableRepository.save(orderTable);
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional(isolation = READ_COMMITTED)
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        checkNullId(orderTableId);
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다"));
        orderService.existsByOrderTableIdAndOrderStatusIn(savedOrderTable.getId(), COULD_NOT_CHANGE_EMPTY_STATUSES);
        savedOrderTable.changeEmpty(true);
        return savedOrderTable;
    }

    @Transactional(isolation = READ_COMMITTED)
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

    public List<OrderTable> findAllByIdIn(List<Long> tableIds) {
        return orderTableRepository.findAllByIdIn(tableIds);
    }
}
