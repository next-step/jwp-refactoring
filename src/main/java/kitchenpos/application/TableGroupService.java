package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public TableGroup create(final TableGroup tableGroup) {
        tableGroup.setCreatedDate(LocalDateTime.now());

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        this.addSavedOrderTables(tableGroup, savedTableGroup);

        return savedTableGroup;
    }

    /**
     * 주문 테이블에 테이블 그룹 정보를 추가 후 저장하고, 테이블 그룹에 해당 테이블을 추가합니다.
     * @param savedTableGroup
     */
    private void addSavedOrderTables(TableGroup tableGroup, TableGroup savedTableGroup) {
        final Long tableGroupId = savedTableGroup.getId();

        for (final OrderTable savedOrderTable : this.findOrderTablesByTableGroup(tableGroup)) {
            savedOrderTable.setTableGroupId(tableGroupId);
            savedOrderTable.setEmpty(false);
            savedTableGroup.addOrderTables(orderTableDao.save(savedOrderTable));
        }
    }

    /**
     * 테이블 그룹에 속한 테이블을 가져옵니다.
     * @param tableGroup
     * @return
     */
    private List<OrderTable> findOrderTablesByTableGroup(TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();
        this.validateOrderTablesSize(orderTables);

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTables.stream()
                                              .map(OrderTable::getId).collect(Collectors.toList()));
        this.validateSavedOrderTables(orderTables, savedOrderTables);

        return savedOrderTables;
    }

    /**
     * 저장 된 테이블들이 유효한지(비어있지 않은지) 확인합니다.
     * @param orderTables
     * @param savedOrderTables
     */
    private void validateSavedOrderTables(List<OrderTable> orderTables, List<OrderTable> savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * 그룹화 하려는 테이블이 없거나, 2개 이상인지 확인합니다.
     * @param orderTables
     */
    private void validateOrderTablesSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
        this.validateOrderTablesByIdAndStatus(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTableDao.save(orderTable);
        }
    }

    /**
     * 주문 테이블들의 ID와 상태가 존재하는지 검사합니다.
     * @param orderTables
     */
    private void validateOrderTablesByIdAndStatus(List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
