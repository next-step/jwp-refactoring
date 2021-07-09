package kitchenpos.table.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.NoTableGroupException;

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
    public TableGroupResponse create(final TableGroupRequest tableGroup) {
        final List<Long> orderTableIds = tableGroup.getOrderTableIds();
        final OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllById(orderTableIds));
        TableGroup result = tableGroupRepository.save(TableGroup.make(orderTableIds, savedOrderTables));
        return TableGroupResponse.of(result);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(NoTableGroupException::new);
        tableGroup.ungroup();
    }
}
