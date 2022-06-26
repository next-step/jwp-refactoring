package kitchenpos.application;

import kitchenpos.domain.OrderTableEntity;
import kitchenpos.domain.TableGroupEntity;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final TableService tableService, final TableGroupRepository tableGroupRepository) {
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        List<OrderTableEntity> orderTables = request.getOrderTableIds()
                .stream()
                .map(id -> tableService.findOrderTableById(id))
                .collect(Collectors.toList());

        TableGroupEntity tableGroup = tableGroupRepository.save(new TableGroupEntity(orderTables));

        return TableGroupResponse.of(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroupEntity tableGroup = findTableGroupById(tableGroupId);

        final List<Long> orderTableIds = tableGroup.getOrderTables()
                .stream()
                .map(OrderTableEntity::getId)
                .collect(Collectors.toList());

        if (tableGroup.getOrderTables().stream()
                .anyMatch(table -> tableService.hasCookingOrMeal(table))) {
            throw new IllegalStateException("조리 혹은 식사 상태인 테이블이 있어서 단체 지정 해제할 수 없습니다: " + tableGroupId);
        }

        tableGroup.ungroup();
    }

    public TableGroupEntity findTableGroupById(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("테이블 그룹을 찾을 수 없습니다: " + id));
    }
}
