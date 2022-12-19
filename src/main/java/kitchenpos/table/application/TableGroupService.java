package kitchenpos.table.application;

import kitchenpos.order.validator.OrderValidator;
import kitchenpos.table.validator.OrderValidatorImpl;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.persistence.OrderTableRepository;
import kitchenpos.table.persistence.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderValidator orderValidator;


    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final OrderValidator orderValidator
    ) {
        this.orderValidator = orderValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        OrderTables orderTables = findAllOrderTableByIds(tableGroupRequest.getOrderTableIds());
        TableGroup tableGroup = tableGroupRepository.save(tableGroupRequest.toTableGroup());
        orderTables.addTableGroup(tableGroup);
        return TableGroupResponse.of(tableGroup, orderTables);
    }

    private OrderTables findAllOrderTableByIds(List<Long> ids) {
        List<OrderTable> orderTables = orderTableRepository.findAllById(ids);
        if (ids.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
        return OrderTables.of(orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = OrderTables.of(orderTableRepository.findAllByTableGroupId(tableGroupId));
        orderValidator.validateOrderComplete(orderTables.getOrderTableIds());
        orderTables.ungroup();
    }
}
