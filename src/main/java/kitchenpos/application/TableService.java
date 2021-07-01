package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.NumberOfGuest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableCreate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public OrderTable create(final OrderTableCreate create) {
        return orderTableDao.save(new OrderTable( create.getNumberOfGuests(), create.isEmpty()));
    }

    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }


    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, boolean empty) {
        final OrderTable orderTable = findById(orderTableId);

        orderTable.changeEmpty(empty);
        return orderTable;
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final NumberOfGuest numberOfGuest) {
        OrderTable orderTable = findById(orderTableId);

        orderTable.changeNumberOfGuest(numberOfGuest);

        return orderTable;
    }

    private OrderTable findById(Long orderTableId) {
        return orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
