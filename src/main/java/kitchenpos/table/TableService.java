package kitchenpos.table;

import kitchenpos.old.dao.OrderDao;
import kitchenpos.old.domain.OrderStatus;
import kitchenpos.table.dto.OrderTable;
import kitchenpos.table.dto.TableAddRequest;
import kitchenpos.table.dto.TableEmptyChangeRequest;
import kitchenpos.table.dto.TableNumberChangeRequest;
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
    public OrderTable create(final TableAddRequest orderTable) {
        return orderTableDao.save(orderTable.toOrderTable());
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final TableEmptyChangeRequest request) {
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        if (orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
        savedOrderTable.changeEmpty(request.isEmpty());
        return orderTableDao.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final TableNumberChangeRequest request) {
        final int numberOfGuests = request.getNumberOfGuests();
        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeNumberOfGuests(numberOfGuests);
        return orderTableDao.save(savedOrderTable);
    }
}
