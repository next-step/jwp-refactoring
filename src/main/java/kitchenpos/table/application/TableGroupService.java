package kitchenpos.table.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.table.dao.TableGroupRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;

@Service
public class TableGroupService {
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;
    private final TableValidator tableValidator;

    public TableGroupService(final TableService tableService, final TableGroupRepository tableGroupRepository, final TableValidator tableValidator) {
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        List<OrderTable> orderTables = tableService.findByOrderTables(request.getOrderTables());
        TableGroup tableGroup = TableGroup.from(orderTables);
        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findByIdWithOrderTable(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 단체지정이 없습니다"));
        for (OrderTable orderTable : tableGroup.getOrderTables().getOrderTables()) {
            tableValidator.checkIsCookingOrMeal(orderTable.getId());
        }
        tableGroup.ungroup();
        tableGroupRepository.delete(tableGroup);
    }
    
    @Transactional(readOnly = true)
    public TableGroup findById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 단체지정이 없습니다"));
    }
}
