package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.error.NotFoundOrderException;
import kitchenpos.order.domain.NumberOfGuests;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderTableEmptyRequest;
import kitchenpos.order.dto.OrderTableNumberOfGuestsRequest;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.order.repository.OrderDao;
import kitchenpos.order.repository.OrderTableDao;
import kitchenpos.tablegroup.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;

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
                .orElseThrow(NotFoundOrderException::new);

        final List<Order> orders = orderDao.findOrdersByOrderTableIdIn(Arrays.asList(orderTableId));

        if (orders.isEmpty()) {
            throw new NotFoundOrderException();
        }

        orders.forEach(Order::checkChangeableStatus);
        orderTable.empty(orderTableRequest.isEmpty());

        return OrderTableResponse.of(orderTable);
    }

    public OrderTableResponse changeNumberOfGuests(final Long orderTableId, final OrderTableNumberOfGuestsRequest orderTableNumberOfGuestsRequest) {
        final NumberOfGuests numberOfGuests = new NumberOfGuests(orderTableNumberOfGuestsRequest.getNumberOfGuests());

        final OrderTable orderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(NotFoundOrderException::new);

        orderTable.checkEmpty();
        orderTable.changeNumberOfGuests(numberOfGuests);

        return OrderTableResponse.of(orderTable);
    }

    public void setGroup(TableGroup tableGroup, List<Long> orderTableIds) {
        final OrderTables orderTables = OrderTables.of(tableGroup, orderTableDao.findAllById(orderTableIds));
        orderTables.setTableGroup(tableGroup);
        orderTableDao.saveAll(orderTables.getOrderTables());
    }

    public void ungroup(TableGroup tableGroup) {
        final OrderTables orderTables = OrderTables.of(tableGroup, orderTableDao.findAllByTableGroupId(tableGroup.getId()));
        List<Order> orders = orderDao.findOrdersByOrderTableIdIn(orderTables.orderIds());

        orders.forEach(Order::checkChangeableStatus);
        orderTables.ungroup();
    }
}
