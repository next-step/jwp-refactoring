package kitchenpos.table.application;

import kitchenpos.order.dao.OrderDao;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableEntity;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupEntity;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.request.OrderTableRequest;
import kitchenpos.table.domain.request.TableGroupRequest;
import kitchenpos.table.domain.response.TableGroupResponse;
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
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderDao orderDao, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTableEntity> orderTables = validateOrderTables(request.getOrderTableIds());

        TableGroupEntity tableGroup = TableGroupEntity.from(orderTables);
        tableGroup.validateTablesEmpty();

        final TableGroupEntity savedTableGroup = tableGroupRepository.save(tableGroup);
        savedTableGroup.tablesMapIntoGroup();

        return TableGroupResponse.toResponse(savedTableGroup);
    }

    private List<OrderTableEntity> validateOrderTables(List<Long> orderTableIds) {
        validateOrderTableSize(orderTableIds);

        final List<OrderTableEntity> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateOrderTableEqualsSize(savedOrderTables, orderTableIds);
        return savedOrderTables;
    }

    private void validateOrderTableSize(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableEqualsSize(List<OrderTableEntity> savedOrderTables, List<Long> orderTableIds)  {
        if (savedOrderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTableEntity> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        validateOrderTablesStatus(orderTables);
        TableGroupEntity tableGroup = TableGroupEntity.from(orderTables);
        tableGroup.unGroup();
    }

    private void validateOrderTablesStatus(List<OrderTableEntity> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
            .map(OrderTableEntity::getId)
            .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
