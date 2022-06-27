package kitchenpos.application;

import kitchenpos.domain.TableGroup;
import kitchenpos.domain.domainService.TableTableGroupDomainService;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableTableGroupDomainService tableTableGroupDomainService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(TableTableGroupDomainService tableTableGroupDomainService,
        TableGroupRepository tableGroupRepository) {
        this.tableTableGroupDomainService = tableTableGroupDomainService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        tableTableGroupDomainService.checkOrderTableForCreateTableGroup(tableGroupRequest);
        TableGroup tableGroup = tableTableGroupDomainService.saveTableGroup(tableGroupRequest);
        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        tableTableGroupDomainService.checkAllMenuIsCompleteInTableGroup(tableGroupId);
        tableTableGroupDomainService.unGroupAllTableInTableGroup(tableGroupId);
    }
}
