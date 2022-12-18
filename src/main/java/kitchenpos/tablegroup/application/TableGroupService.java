package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.OrderValidator;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.utils.Message.NOT_EXISTS_ORDER_TABLE;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    private final OrderValidator orderValidator;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository, OrderValidator orderValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderValidator = orderValidator;
    }


    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final OrderTables savedOrderTables = findOrderTablesByIds(request);
        TableGroup saveTableGroup = tableGroupRepository.save(TableGroup.createEmpty());
        savedOrderTables.registerTableGroup(saveTableGroup);
        return TableGroupResponse.of(saveTableGroup, savedOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = OrderTables.from(orderTableRepository.findAllByTableGroupId(tableGroupId));
        orderValidator.checkCanBeUngrouped(orderTables.getOrderTableIds());
        orderTables.unGroup();
    }

    private OrderTables findOrderTablesByIds(TableGroupRequest request) {
        return OrderTables.from(
                request.getOrderTableIds()
                        .stream()
                        .map(this::findOrderTableById)
                        .collect(Collectors.toList())
        );
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_ORDER_TABLE));
    }
}
