package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;

    public TableService(final OrderDao orderDao,
                        final OrderTableRepository orderTableRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderTable create(final OrderTable orderTable) {
        orderTable.ungroup();

        return orderTableRepository.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableRepository.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("등록이 되지 않은 주문테이블은 상태를 변경할 수 없습니다."));

        if (savedOrderTable.isGrouped()) {
            throw new IllegalArgumentException("그룹 설정이 되어 있는 테이블은 주문 등록 불가 상태로 바꿀 수 없습니다.");
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("조리상태이거나 식사상태주문의 주문테이블은 상태를 변경할 수 없습니다.");
        }

        savedOrderTable.setEmpty(orderTable.isEmpty());

        return orderTableRepository.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final int numberOfGuests = orderTable.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("방문 손님을 음수로 수정할 수 없습니다.");
        }

        final OrderTable savedOrderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("등록이 안된 주문테이블은 방문 손님 수를 수정할 수 없습니다."));

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 방문 손님 수를 수정할 수 없습니다.");
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return orderTableRepository.save(savedOrderTable);
    }
}
