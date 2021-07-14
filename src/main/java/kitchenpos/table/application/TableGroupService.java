package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.NotFoundOrderTableException;
import kitchenpos.table.exception.NotFoundTableGroupException;
import kitchenpos.table.util.TableGroupMapper;
import kitchenpos.table.util.TableGroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private final TableGroupMapper tableGroupMapper;
    private final TableGroupValidator tableGroupValidator;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(TableGroupMapper tableGroupMapper, TableGroupValidator tableGroupValidator, TableGroupRepository tableGroupRepository) {
        this.tableGroupMapper = tableGroupMapper;
        this.tableGroupValidator = tableGroupValidator;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        TableGroup tableGroup = tableGroupMapper.mapFormToTableGroup(tableGroupRequest);
        tableGroup.validateGroup(tableGroupValidator);
        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NotFoundTableGroupException());
        tableGroup.validateUngroup(tableGroupValidator);
        /*final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        final List<Order> orders = orderRepository.findByOrderTableIdIn(orderTableIds);
        orders.forEach(Order::ungroup);*/
        tableGroupRepository.deleteById(tableGroupId);
    }

    private TableGroup toTableGroup(List<OrderTable> orderTables, TableGroupRequest tableGroupRequest) {
        if (!tableGroupRequest.isSameOrderTableCount(orderTables.size())) {
            throw new NotFoundOrderTableException();
        }
        return new TableGroup(new OrderTables(orderTables));
    }
}
