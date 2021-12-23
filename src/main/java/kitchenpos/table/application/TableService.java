package kitchenpos.table.application;

import java.util.List;
import kitchenpos.common.exception.NoResultDataException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.table.dto.ChangeEmptyRequest;
import kitchenpos.table.dto.ChangeNumberOfGuestRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableService {

    private final OrderTableDao orderTableDao;
    private final OrderDao orderDao;

    public TableService(OrderTableDao orderTableDao, OrderDao orderDao) {
        this.orderTableDao = orderTableDao;
        this.orderDao = orderDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        return OrderTableResponse.of(orderTableDao.save(orderTableRequest.toOrderTable()));
    }

    @Transactional(readOnly = true)
    public List<OrderTable> list() {
        return orderTableDao.findAll();
    }

    @Transactional
    public OrderTableResponse changeEmpty(final Long orderTableId, final ChangeEmptyRequest changeEmptyRequest) {

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(NoResultDataException::new);

        OrderStatus savedOrderStatus = orderDao.findByOrderTableId(savedOrderTable.getId());
        OrderStatus.validStatusIsCookingOrMealThrow(savedOrderStatus);

        savedOrderTable.changeOrderTableStatus(changeEmptyRequest.isEmpty());

        return OrderTableResponse.of(savedOrderTable);
    }

    @Transactional
    public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
        final ChangeNumberOfGuestRequest changeEmptyRequest) {

        final OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrderTable.changeNumberOfGuest(changeEmptyRequest.getNumberOfGuest());
        return OrderTableResponse.of(savedOrderTable);
    }
}
