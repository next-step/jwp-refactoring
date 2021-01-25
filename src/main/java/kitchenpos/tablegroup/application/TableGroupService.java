package kitchenpos.tablegroup.application;

import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final TableService tableService, final TableGroupRepository tableGroupRepository) {
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        OrderTables orderTables = tableService.findOrderTablesById(tableGroupRequest.getOrderTableIds());

        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());

        orderTables.group(savedTableGroup);

        return TableGroupResponse.of(savedTableGroup, orderTables);
    }

//    @Transactional
//    public void ungroup(final Long tableGroupId) {
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
//    }
}
