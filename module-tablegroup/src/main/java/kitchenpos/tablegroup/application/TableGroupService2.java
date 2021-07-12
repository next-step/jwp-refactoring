package kitchenpos.tablegroup.application;

import kitchenpos.table.domain.OrderTableEntity;
import kitchenpos.table.domain.TableRepository;
import kitchenpos.tablegroup.domain.TableGroupEntity;
import kitchenpos.tablegroup.domain.TableGroupExternalValidator;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService2 {

    private static final int MIN_GROUP_SIZE = 2;

    private final TableGroupExternalValidator tableGroupExternalValidator;
    private final TableRepository tableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService2(final TableGroupExternalValidator tableGroupExternalValidator, final TableRepository tableRepository, final TableGroupRepository tableGroupRepository) {
        this.tableGroupExternalValidator = tableGroupExternalValidator;
        this.tableRepository = tableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        List<OrderTableEntity> savedOrderTables = getTablesByIds(request.extractTableIds());
        TableGroupEntity savedGroup = tableGroupRepository.save(new TableGroupEntity());
        savedOrderTables.forEach(table -> table.bindTableGroup(savedGroup.getId()));
        return TableGroupResponse.of(savedGroup, savedOrderTables);
    }

    private List<OrderTableEntity> getTablesByIds(List<Long> tableIds) {
        validateTableCount(tableIds);
        List<OrderTableEntity> savedOrderTables = tableRepository.findAllByIdIn(tableIds);
        validateDuplicatedTables(tableIds, savedOrderTables);
        return savedOrderTables;
    }

    private void validateDuplicatedTables(List<Long> tableIds, List<OrderTableEntity> savedOrderTables) {
        if (tableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateTableCount(List<Long> tableIds) {
        if (CollectionUtils.isEmpty(tableIds) || tableIds.size() < MIN_GROUP_SIZE) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        List<OrderTableEntity> tables = tableRepository.findAllByTableGroupId(tableGroupId);
        List<Long> tableIds = tables.stream().map(OrderTableEntity::getId).collect(Collectors.toList());
        tableGroupExternalValidator.validateTablesInUse(tableIds);
        tables.forEach(OrderTableEntity::ungroup);
    }
}
