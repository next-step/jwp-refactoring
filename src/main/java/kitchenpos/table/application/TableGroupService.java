package kitchenpos.table.application;

import kitchenpos.table.domain.*;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.NotFoundTableGroupException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableDomainService tableDomainService;
    private final TableGroupCreatingValidator tableGroupCreatingValidator;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(TableDomainService tableDomainService,
                             TableGroupCreatingValidator tableGroupCreatingValidator,
                             TableGroupRepository tableGroupRepository) {
        this.tableDomainService = tableDomainService;
        this.tableGroupCreatingValidator = tableGroupCreatingValidator;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest request) {
        List<OrderTable> orderTables = tableDomainService.findAllIdIn(request.toOrderTableIds());
        tableGroupCreatingValidator.validate(request, orderTables);
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
