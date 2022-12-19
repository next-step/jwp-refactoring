package kitchenpos.table.application;

import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupValidator;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;

@Service
public class TableGroupService {
    private final TableGroupValidator tableGroupValidator;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupMapper tableGroupMapper;

    public TableGroupService(TableGroupValidator tableGroupValidator, TableGroupRepository tableGroupRepository, TableGroupMapper tableGroupMapper) {
        this.tableGroupValidator = tableGroupValidator;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupMapper = tableGroupMapper;
    }


    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final TableGroup tableGroup = tableGroupMapper.mapFrom(tableGroupRequest);
        tableGroup.enGroup(tableGroupValidator);
        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));

    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(NoResultException::new);
        tableGroup.unGroup(tableGroupValidator);
        tableGroupRepository.save(tableGroup);
    }
}
