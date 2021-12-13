package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.TableRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTable create(final TableRequest request) {
        return orderTableDao.save(request.toOrderTable());
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final TableRequest request) {
        checkCompleteTable(orderTableId);

        final OrderTable table = getOrderTable(orderTableId);
        table.checkInTableGroup();
        table.changeEmpty(request.isEmpty());

        return table;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final TableRequest request) {
        final OrderTable table = getOrderTable(orderTableId);
        table.checkEmpty();
        table.changeNumberOfGuests(request.getNumberOfGuests());

        return table;
    }

    private void checkCompleteTable(Long orderTableId) {
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    private OrderTable getOrderTable(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
