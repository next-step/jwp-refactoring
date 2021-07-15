package kitchenpos.tablegroup.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.generic.exception.TableGroupNotFoundException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableGroupValidator;
import kitchenpos.tablegroup.dto.TableGroupRequest;

@Service
public class TableGroupService {
    private final TableGroupValidator validator;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(TableGroupValidator validator, TableGroupRepository tableGroupRepository) {
        this.validator = validator;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest request) {
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        tableGroup.group(validator, request.getOrderTableIds());
        return tableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findById(tableGroupId);
        tableGroup.ungroup(validator);
        tableGroupRepository.delete(tableGroup);
    }

    private TableGroup findById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new TableGroupNotFoundException("해당 ID의 테이블 그룹이 존재하지 않습니다."));
    }
}
