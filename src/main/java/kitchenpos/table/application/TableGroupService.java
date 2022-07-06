package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    @Transactional
    public TableGroupResponse create(final TableGroup tableGroup) {
        for (OrderTable orderTable : tableGroup.getOrderTables()) {

        }
        return null;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
//        final List<TableGroupResponse> orderTables = orderTableDao.findAllByTableGroupId(tableGroupId);
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
////            orderTable.setTableGroupId(null);
//            orderTableDao.save(orderTable);
//        }
    }
}
