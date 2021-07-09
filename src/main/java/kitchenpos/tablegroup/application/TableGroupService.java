package kitchenpos.tablegroup.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.error.InvalidRequestException;
import kitchenpos.common.error.NotFoundTableGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderDao;
import kitchenpos.ordertable.repository.OrderTableDao;
import kitchenpos.tablegroup.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupDao;

@Service
public class TableGroupService {
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
        final List<Long> orderTableIds = tableGroupRequest.ids();

        final TableGroup tableGroup = new TableGroup();
        tableGroupDao.save(tableGroup);

        final OrderTables orderTables = OrderTables.of(tableGroup, orderTableDao.findAllById(orderTableIds));

        if (orderTables.size() != orderTableIds.size()) {
            throw new InvalidRequestException();
        }

        orderTableDao.saveAll(orderTables.getOrderTables());
        orderTables.setTableGroup(tableGroup);

        return TableGroupResponse.of(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId).orElseThrow(NotFoundTableGroup::new);

        final OrderTables orderTables = OrderTables.of(tableGroup, orderTableDao.findAllByTableGroupId(tableGroupId));
        List<Order> orders = orderDao.findOrdersByOrderTableIdIn(orderTables.orderIds());

        orders.forEach(Order::checkChangeableStatus);
        orderTables.ungroup();
    }
}
