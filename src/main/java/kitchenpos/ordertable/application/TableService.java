package kitchenpos.ordertable.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.ordertable.dto.OrderTableEmptyRequest;
import kitchenpos.ordertable.dto.OrderTableNumberOfGuestsRequest;
import kitchenpos.common.error.CustomException;
import kitchenpos.common.error.ErrorInfo;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderDao;
import kitchenpos.ordertable.domain.NumberOfGuests;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.repository.OrderTableDao;

@Service
@Transactional
public class TableService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;

    public TableService(final OrderDao orderDao, final OrderTableDao orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        OrderTable orderTable = OrderTable.of(new NumberOfGuests(orderTableRequest.getNumberOfGuests()), orderTableRequest.isEmpty());
        return OrderTableResponse.of(orderTableDao.save(orderTable));
    }

    @Transactional(readOnly = true)
    public List<OrderTableResponse> list() {
        return OrderTableResponse.listOf(orderTableDao.findAll());
    }

    public OrderTableResponse changeEmpty(final Long orderTableId, final OrderTableEmptyRequest orderTableRequest) {
        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new CustomException(ErrorInfo.NOT_FOUND_ORDER_TABLE));

        final List<Order> orders = orderDao.findByOrderTableId(orderTableId);

        if (orders.isEmpty()) {
            throw new CustomException(ErrorInfo.NOT_FOUND_ORDER);
        }

        orders.forEach(Order::checkChangeableStatus);
        orderTable.checkExistsTableGroup();
        orderTable.empty(orderTableRequest.isEmpty());

        return OrderTableResponse.of(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableNumberOfGuestsRequest orderTableNumberOfGuestsRequest) {
        final NumberOfGuests numberOfGuests = new NumberOfGuests(orderTableNumberOfGuestsRequest.getNumberOfGuests());

        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(() -> new CustomException(ErrorInfo.NOT_FOUND_ORDER_TABLE));

        orderTable.checkEmpty();
        orderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.of(orderTable);
    }
}
