package kitchenpos.tablegroup;

import kitchenpos.table.OrderTableRepository;
import kitchenpos.table.OrderTables;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final OrderTables orderTables) {
        orderTables.validateForCreatableGroup();
        final OrderTables savedOrderTables =
            OrderTables.of(orderTableRepository.findAllById(orderTables.extractIds()));
        savedOrderTables.validateForCreatableGroup(orderTables);

        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.create());
        savedOrderTables.groupBy(savedTableGroup);

        return TableGroupResponse.from(savedTableGroup, savedOrderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(IllegalArgumentException::new);
        final OrderTables orderTables = OrderTables.of(orderTableRepository.findAllByTableGroupId(
            tableGroupId));

        tableGroup.Ungroup(orderTables);
        tableGroupRepository.delete(tableGroup);
    }

}
