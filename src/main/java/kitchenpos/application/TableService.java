package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class TableService {
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
        this.canEmptySavedOrderTable(orderTableId, savedOrderTable);

        savedOrderTable.setEmpty(orderTable.isEmpty());

        return orderTableDao.save(savedOrderTable);
    }

    /**
     * 비울 수 있는 테이블인지 확인합니다.
     * @param orderTableId
     * @param savedOrderTable
     */
    private void canEmptySavedOrderTable(Long orderTableId, OrderTable savedOrderTable) {
        this.notExistTabeGroup(savedOrderTable);
        this.validateOrderTablesByIdsAndStatus(orderTableId);
    }

    /**
     * 테이블그룹화 된 테이블인지 확인합니다.
     * @param savedOrderTable
     */
    private void notExistTabeGroup(OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 주문 테이블들의 ID들과 상태가 적합한지 검사합니다.
     * @param orderTableId
     */
    private void validateOrderTablesByIdsAndStatus(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        final int numberOfGuests = this.validateNumberOfGuests(orderTable);

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);

        this.checkEmptySavedOrderTable(savedOrderTable);

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return orderTableDao.save(savedOrderTable);
    }

    /**
     * 주문 테이블이 비었는지 확인합니다.
     * @param savedOrderTable
     * @throws IllegalArgumentException
     */
    private void checkEmptySavedOrderTable(OrderTable savedOrderTable) {
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 수정하려는 손님의 수가 적합한지 체크하고, 그 값을 반환합니다.
     * @param orderTable
     * @return
     * @throws IllegalArgumentException
     */
    private int validateNumberOfGuests(OrderTable orderTable) {
        final int numberOfGuests = orderTable.getNumberOfGuests();

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        return numberOfGuests;
    }
}
