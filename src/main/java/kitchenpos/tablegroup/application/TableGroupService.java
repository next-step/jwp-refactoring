package kitchenpos.tablegroup.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.error.CustomException;
import kitchenpos.common.error.ErrorInfo;
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
        final OrderTables orderTables = OrderTables.of(orderTableDao.findAllById(orderTableIds));

        if (orderTables.size() != orderTableIds.size()) {
            throw new CustomException(ErrorInfo.INVALID_REQUEST_ORDER_TABLE_SIZE);
        }

        final TableGroup tableGroup = TableGroup.of(orderTables);

        tableGroupDao.save(tableGroup);
        tableGroup.initOrderTable();

        return TableGroupResponse.of(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = OrderTables.of(orderTableDao.findAllByTableGroupId(tableGroupId));

        List<Long> orderTableIds = orderTables.orderTableIds();
        List<Order> orders = orderDao.findByOrderTableId(orderTableIds);

        orders.forEach(Order::checkChangeableStatus);
        orderTables.unGroup();
    }
}
