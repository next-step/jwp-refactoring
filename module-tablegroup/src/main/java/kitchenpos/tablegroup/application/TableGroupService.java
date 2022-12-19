package kitchenpos.tablegroup.application;

import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.*;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.exception.NotFoundTableGroupException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupCreatingValidator;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableService tableService;
    private final TableGroupCreatingValidator tableGroupCreatingValidator;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(TableService tableService,
                             TableGroupCreatingValidator tableGroupCreatingValidator,
                             TableGroupRepository tableGroupRepository) {
        this.tableService = tableService;
        this.tableGroupCreatingValidator = tableGroupCreatingValidator;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest request) {
        List<OrderTable> orderTables = tableService.findAllIdIn(request.toOrderTableIds());
        tableGroupCreatingValidator.validate(request.toOrderTableIds(), orderTables);
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        tableGroup.addOrderTables(orderTables);
        return TableGroupResponse.of(tableGroup);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                                                    .orElseThrow(NotFoundTableGroupException::new);
        tableGroup.ungroup();
        tableGroupRepository.save(tableGroup);
    }
}
