package kitchenpos.application.table;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.dto.table.TableGroupRequest;
import kitchenpos.dto.table.TableGroupResponse;
import kitchenpos.repository.table.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableValidator tableValidator;

    public TableGroupService(final TableGroupRepository tableGroupRepository, TableValidator tableValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> savedOrderTables = tableValidator.findTableAllByIdIn(tableGroupRequest.getOrderTableIds());

        tableValidator.orderTablesSizeValidation(savedOrderTables, tableGroupRequest);

        tableValidator.addOrderTableValidation(savedOrderTables);
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(savedOrderTables));

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.addTableGroup(tableGroup.getId());
        }

        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(IllegalArgumentException::new);

        tableValidator.orderStatusByIdsValidate(tableGroup.getOrderTables().orderTableIds());

        tableGroup.unGroup();
    }
}
