package kitchenpos.tableGroup.application;

import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.dto.TableGroupCreateRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
import kitchenpos.tableGroup.mapper.TableGroupMapper;
import kitchenpos.tableGroup.repository.TableGroupRepository;
import kitchenpos.tableGroup.validator.TableGroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;
    private final TableGroupMapper tableGroupMapper;

    public TableGroupService(
            final TableGroupRepository tableGroupRepository,
            final TableGroupValidator tableGroupValidator,
            final TableGroupMapper tableGroupMapper
    ) {
        this.tableGroupValidator = tableGroupValidator;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupMapper = tableGroupMapper;
    }

    public TableGroup getTableGroup(final Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 에 해당하는 단체 지정 정보를 찾을 수 없습니다."));
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        return TableGroupResponse.from(
                tableGroupRepository.save(tableGroupMapper.mapFrom(request))
        );
    }

    @Transactional
    public void ungroup(final Long id) {
        TableGroup tableGroup = getTableGroup(id);
        tableGroupValidator.possibleUngroupTableGroup(tableGroup);

        tableGroup.ungroup();
    }
}
