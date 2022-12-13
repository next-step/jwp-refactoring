package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    public OrderTableResponse create(final OrderTable orderTable) {
        orderTable.setTableGroupId(null);

        return OrderTableResponse.of(orderTableDao.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return orderTableDao.findAll().stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderTable findById(Long orderTableId){
        return orderTableDao.findById(orderTableId).orElseThrow(IllegalArgumentException::new);
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        final OrderTable savedOrderTable = findById(orderTableId);
        savedOrderTable.validateTableGroupId();
        validateOrderStatus(orderTableId);

        savedOrderTable.setEmpty(orderTable.isEmpty());

        return OrderTableResponse.of(orderTableDao.save(savedOrderTable));
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        validateNumberOfGuests(orderTable.getNumberOfGuests());

        final int numberOfGuests = orderTable.getNumberOfGuests();

        final OrderTable savedOrderTable = findById(orderTableId);
        validateEmptyTrue(savedOrderTable);

        savedOrderTable.setNumberOfGuests(numberOfGuests);

        return OrderTableResponse.of(orderTableDao.save(savedOrderTable));
    }

    private void validateOrderStatus(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateEmptyTrue(OrderTable savedOrderTable){
        if (savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}
