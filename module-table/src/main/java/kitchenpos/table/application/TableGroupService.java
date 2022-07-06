package kitchenpos.table.application;

import java.util.List;
import kitchenpos.common.exception.NotExistException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderTableRepository orderTableRepository,
                             TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllById(
                tableGroupRequest.getOrderTableIds());
        final TableGroup tableGroup = new TableGroup(savedOrderTables);
        final TableGroup persistTableGroup = tableGroupRepository.save(tableGroup);
        return persistTableGroup.toTableGroupResponse();
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup persistTableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(NotExistException::new);
        persistTableGroup.ungroup();
    }
}
