package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupCreator tableGroupCreator;

    public TableGroupService(OrderRepository orderRepository,
                             OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository,
                             TableGroupCreator tableGroupCreator) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupCreator = tableGroupCreator;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {
        TableGroup tableGroup = tableGroupCreator.toTableGroup(tableGroupRequest);
        OrderTables orderTables = tableGroup.getOrderTables();
        orderTables.validateCreate();
        orderTables.setAllEmpty(false);

        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(orderTables);

        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        checkTablesCookingOrMeal(orderTables);

        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(null);
            orderTableRepository.save(orderTable);
        }
    }

    private void checkTablesCookingOrMeal(List<OrderTable> orderTables) {
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("조리중 또는 식사중인 테이블이 존재합니다");
        }
    }
}
