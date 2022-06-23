package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orderTable.OrderTableRepository;
import kitchenpos.domain.orderTable.OrderTables;
import kitchenpos.domain.tableGroup.TableGroup;
import kitchenpos.domain.tableGroup.TableGroupRepository;
import kitchenpos.dto.tableGroup.TableGroupRequest;
import kitchenpos.dto.tableGroup.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderDao orderDao, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        OrderTables orderTables = new OrderTables(tableGroupRequest.getOrderTables());

        final List<Long> orderTableIds = orderTables.findOrderTableIds();
        final OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));

        orderTables.validateForCreateTableGroup(savedOrderTables);
        savedOrderTables.updateEmpty(false);

        TableGroup tableGroup = TableGroup.of(tableGroupRequest);
        tableGroup.addOrderTables(savedOrderTables);

        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
                orderTables.findOrderTableIds(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException();
        }

        orderTables.updateTableGroup(null);
    }
}
