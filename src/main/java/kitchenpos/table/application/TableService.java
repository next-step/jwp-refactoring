package kitchenpos.table.application;

import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.ChangeNumberOfGuestsRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class TableService {
    public static final String CHANGE_NUMBER_OF_GUESTS_MINIMUM_NUMBER_EXCEPTION_MESSAGE = "변경하는 손님수는 0명보다 작을 수 없습니다.";
    public static final int CHANGE_NUMBER_OF_GUESTS_MINIMUM_NUMBER = 0;
    public static final String TABLE_NOT_EMPTY_EXCEPTION_MESSAGE = "테이블이 공석 상태면 손님수를 변경할 수 없다.";
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTable create(final OrderTable orderTable) {
        orderTable.setTableGroupId(null);

        return orderTableDao.save(orderTable);
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }

        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        savedOrderTable.setEmpty(orderTable.isEmpty());

        return orderTableDao.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final ChangeNumberOfGuestsRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();

        if (numberOfGuests < CHANGE_NUMBER_OF_GUESTS_MINIMUM_NUMBER) {
            throw new IllegalArgumentException(CHANGE_NUMBER_OF_GUESTS_MINIMUM_NUMBER_EXCEPTION_MESSAGE);
        }

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException(TABLE_NOT_EMPTY_EXCEPTION_MESSAGE);
        }

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return orderTableDao.save(savedOrderTable);
    }
}
