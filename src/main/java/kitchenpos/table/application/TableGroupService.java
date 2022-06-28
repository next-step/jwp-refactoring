package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.TableRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableRepository tableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableService tableService;

    public TableGroupService(final TableRepository tableRepository, final TableGroupRepository tableGroupRepository, TableService tableService) {
        this.tableRepository = tableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableService = tableService;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        TableGroup tableGroup = tableGroupRepository.save(toEntity(request));
        return TableGroupResponse.of(tableGroup);
    }

    private TableGroup toEntity(final TableGroupRequest request) {
        List<OrderTable> orderTables = request.getOrderTableIds().stream()
                .map(id -> tableRepository.getById(id))
                .collect(Collectors.toList());

        return new TableGroup(orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.getById(tableGroupId);

        final List<Long> orderTableIds = tableGroup.getOrderTables()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (tableGroup.getOrderTables().stream()
                .anyMatch(table -> tableService.hasCookingOrMeal(table))) {
            throw new IllegalStateException("조리 혹은 식사 상태인 테이블이 있어서 단체 지정 해제할 수 없습니다. id: " + tableGroupId);
        }

        tableGroup.ungroup();
    }
}
