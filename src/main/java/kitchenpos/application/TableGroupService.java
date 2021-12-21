package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.exception.TableGroupNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        validateOrderTablesSize(tableGroupRequest);

        List<OrderTable> orderTables = orderTableRepository.findAllById(tableGroupRequest.getOrderTableIds());
        TableGroup tableGroup = new TableGroup(orderTables, tableGroupRequest.getOrderTablesSize());
        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    private void validateOrderTablesSize(TableGroupRequest tableGroupRequest) {
        if (tableGroupRequest.isEmptyOrderTables() || tableGroupRequest.getOrderTablesSize() < 2) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new TableGroupNotFoundException(tableGroupId));
        tableGroup.ungroup();
    }
}
