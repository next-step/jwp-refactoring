package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.Orders;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;

@Service
public class TableGroupService {
    private static final int MINIMUM_TABLE_SIZE = 2;

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        validateRequest(tableGroupRequest);
        final List<Long> ids = tableGroupRequest.ids();
        final OrderTables orderTables = new OrderTables(findOrderTables(ids));
        validateOrderTableSize(ids, orderTables);
        final TableGroup saved = saveTableGroup(orderTables);

        return TableGroupResponse.of(saved);
    }

    private void validateRequest(TableGroupRequest tableGroupRequest) {
        if (tableGroupRequest.isEmptyOrderTables() || tableGroupRequest.orderTablesSize() < MINIMUM_TABLE_SIZE) {
            throw new IllegalArgumentException();
        }
    }

    private List<OrderTable> findOrderTables(List<Long> ids) {
        return orderTableDao.findAllByIdIn(ids);
    }

    private void validateOrderTableSize(List<Long> ids, OrderTables orderTables) {
        if (ids.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private TableGroup saveTableGroup(OrderTables orderTables) {
        return tableGroupDao.save(new TableGroup(orderTables));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = new OrderTables(findAllOrderTable(tableGroupId));
        final Orders orders = new Orders(findAllOrder(orderTables));
        orderTables.ungroup(orders);
    }

    private List<OrderTable> findAllOrderTable(Long tableGroupId) {
        return orderTableDao.findAllByTableGroup_Id(tableGroupId);
    }

    private List<Order> findAllOrder(OrderTables orderTables) {
        final List<Long> orderTableIds = orderTables.ids();

        return orderDao.findAllByOrderTable_IdIn(orderTableIds);
    }
}
