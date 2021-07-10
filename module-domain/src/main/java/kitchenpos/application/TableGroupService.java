package kitchenpos.application;

import kitchenpos.domain.table.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public Long create(final TableGroupCreate tableGroupCreate) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllById(tableGroupCreate.getOrderTableIds()));

        return tableGroupRepository.save(TableGroup.create(tableGroupCreate, orderTables))
                .getId();
    }

    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        tableGroup.ungroup();
    }
}
