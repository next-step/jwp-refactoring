package kitchenpos.tablegroup.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.table.application.OrderTableService;
import kitchenpos.table.dao.OrderTableDao;
import kitchenpos.table.domain.OrderTableEntity;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.tablegroup.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupEntity;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kitchenpos.table.domain.OrderTables.MINIMUM_SIZE;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    private final OrderTableService orderTableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderDao orderDao, OrderTableDao orderTableDao, TableGroupDao tableGroupDao, OrderTableService orderTableService, TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.orderTableService = orderTableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("주문 테이블이 2개 이상이어야 합니다.");
        }

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableDao.findAllByIdIn(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("등록되지 않은 주문 테이블이 있습니다.");
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            isEmptyCheck(savedOrderTable);
            hasTableGroupIdCheck(savedOrderTable);
        }

        tableGroup.setCreatedDate(LocalDateTime.now());

        final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        final Long tableGroupId = savedTableGroup.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.setTableGroupId(tableGroupId);
            savedOrderTable.setEmpty(false);
            orderTableDao.save(savedOrderTable);
        }
        savedTableGroup.setOrderTables(savedOrderTables);

        return savedTableGroup;
    }

    @Transactional
    public TableGroupResponse createTemp(TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = getOrderTableIds(tableGroupRequest.getOrderTables());
        final OrderTables savedOrderTables = findOrderTables(orderTableIds);
        TableGroupEntity tableGroupEntity = new TableGroupEntity(savedOrderTables);
        return TableGroupResponse.of(tableGroupRepository.save(tableGroupEntity));
    }

    private OrderTables findOrderTables(List<Long> orderTableIds) {
        List<OrderTableEntity> savedOrderTables = orderTableService.findAllByIdIn(orderTableIds);
        allRegisteredCheck(orderTableIds, savedOrderTables);
        return new OrderTables(savedOrderTables);
    }

    private void allRegisteredCheck(List<Long> orderTableIds, List<OrderTableEntity> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("등록되지 않은 주문 테이블이 있습니다.");
        }
    }

    private List<Long> getOrderTableIds(List<OrderTableRequest> orderTables) {
        sizeValidCheck(orderTables);
        return orderTables.stream().map(OrderTableRequest::getId).collect(Collectors.toList());
    }

    private void sizeValidCheck(List<OrderTableRequest> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_SIZE) {
            throw new IllegalArgumentException("주문 테이블이 2개 이상이어야 합니다.");
        }
    }

    private void isEmptyCheck(OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 주문 테이블이 아닙니다.");
        }
    }

    private void hasTableGroupIdCheck(OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroupId())) {
            throw new IllegalArgumentException("이미 단체 지정된 테이블이 있습니다.");
        }
    }


    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문이 조리나 식사 상태에서는 변경할 수 없습니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(null);
            orderTableDao.save(orderTable);
        }
    }


}
