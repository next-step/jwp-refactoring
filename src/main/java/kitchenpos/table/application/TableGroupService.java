package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao,
        final TableGroupDao tableGroupDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroup tableGroup) {
        validateTableGroup(tableGroup);

        final List<OrderTable> savedOrderTables = orderTableDao
            .findAllByIdIn(getOrderTableIds(tableGroup));
        validateExists(tableGroup, savedOrderTables);
        validateOrderTablesStatus(savedOrderTables);

        tableGroup.setCreatedDate(LocalDateTime.now());

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        updateOrderTables(savedOrderTables, savedTableGroup.getId());

        savedTableGroup.setOrderTables(savedOrderTables);
        return TableGroupResponse.from(savedTableGroup);
    }

    private void updateOrderTables(List<OrderTable> savedOrderTables, Long tableGroupId) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroupId(tableGroupId);
            savedOrderTable.setEmpty(false);
            orderTableDao.save(savedOrderTable);
        }
    }

    private void validateExists(TableGroup tableGroup, List<OrderTable> savedOrderTables) {
        if (tableGroup.getOrderTables().size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTablesStatus(List<OrderTable> orderTables) {
        for (final OrderTable savedOrderTable : orderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void validateTableGroup(TableGroup tableGroup) {
        if (CollectionUtils.isEmpty(tableGroup.getOrderTables())
            || tableGroup.getOrderTables().size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private List<Long> getOrderTableIds(TableGroup tableGroup) {
        return tableGroup.getOrderTables().stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.findById(tableGroupId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블 그룹 입니다."));
        tableGroup.ungroup();
    }
}
