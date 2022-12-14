package kitchenpos.application;

import static kitchenpos.exception.ErrorCode.NOT_COMPLETION_STATUS;
import static kitchenpos.exception.ErrorCode.TABLE_IS_NOT_EMPTY_OR_ALREADY_REGISTER_TABLE_GROUP;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.exception.KitchenposException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(
            final OrderDao orderDao,
            final OrderTableDao orderTableDao,
            final TableGroupDao tableGroupDao
    ) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    public TableGroupResponse create(final TableGroup tableGroup) {
        tableGroup.validateOrderTables();

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(tableGroup.getOrderTableIds());
        tableGroup.validateOrderTablesSize(savedOrderTables.size());
        validateOrderTables(savedOrderTables);

        tableGroup.setCreatedDate(LocalDateTime.now());

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        saveOrderTables(savedTableGroup.getId(), savedOrderTables);

        savedTableGroup.setOrderTables(savedOrderTables);

        return TableGroupResponse.of(savedTableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        validateOrderStatus(getOrderTableIds(orderTables));

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTableDao.save(orderTable);
        }
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new KitchenposException(TABLE_IS_NOT_EMPTY_OR_ALREADY_REGISTER_TABLE_GROUP);
            }
        }
    }

    private void saveOrderTables(Long tableGroupId, List<OrderTable> savedOrderTables){
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroupId(tableGroupId);
            savedOrderTable.setEmpty(false);
            orderTableDao.save(savedOrderTable);
        }
    }

    private List<Long> getOrderTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    private void validateOrderStatus(List<Long> orderTableIds){
        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new KitchenposException(NOT_COMPLETION_STATUS);
        }
    }
}
