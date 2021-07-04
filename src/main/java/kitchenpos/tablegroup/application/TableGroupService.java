package kitchenpos.tablegroup.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;

@Service
public class TableGroupService {
    private TableGroupRepository tableGroupRepository;
    private OrderTableRepository orderTableRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroup) {
        final List<Long> orderTableIds = tableGroup.getOrderTableIds();
        final OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllById(orderTableIds));
        return tableGroupRepository.save(TableGroup.make(orderTableIds, savedOrderTables));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);
        tableGroup.ungroup();
    }
}
