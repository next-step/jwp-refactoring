package kitchenpos.table.application;

import kitchenpos.order.domain.OrderDao;
import kitchenpos.table.domain.OrderTableDao;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroupDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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
        final List<Long> orderTableIds = tableGroupRequest.getOrderTables();

        if(CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }

        TableGroup tableGroup = new TableGroup();

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        if(orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        OrderTables orderTables = getOrderTables(tableGroup, savedOrderTables);
        tableGroup.updateOrderTables(orderTables);

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
//        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
//
//        final List<Long> orderTableIds = orderTables.stream()
//                .map(OrderTable::getId)
//                .collect(Collectors.toList());
//
//        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
//                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
//            throw new IllegalArgumentException();
//        }
//
//        for (final OrderTable orderTable : orderTables) {
//            orderTable.setTableGroupId(null);
//            orderTableDao.save(orderTable);
//        }
    }

    private OrderTables getOrderTables(TableGroup tableGroup, List<OrderTable> savedOrderTables) {
        OrderTables orderTables = new OrderTables();
        for(final OrderTable savedOrderTable: savedOrderTables) {
            validateOrderTable(savedOrderTable);

            savedOrderTable.update(tableGroup, false);
            orderTables.addOrderTable(savedOrderTable);
        }

        return orderTables;
    }

    private void validateOrderTable(OrderTable orderTable) {
        if(orderTable.isAlreadyRegistered()) {
            throw new IllegalArgumentException();
        }
    }
}
