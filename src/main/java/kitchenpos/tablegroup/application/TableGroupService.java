package kitchenpos.tablegroup.application;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupValidator;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.exception.CanNotUnGroupException;
import kitchenpos.tablegroup.infra.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {
    private static final String NOT_FOUND_TABLE_GROUP_ERROR_MESSAGE = "해당 단체 지정을 찾지 못하여 해산할 수 없습니다.";

    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(TableGroupRepository tableGroupRepository, TableGroupValidator tableGroupValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final TableGroup tableGroup = TableGroup.create(request.getOrderTableIds(), tableGroupValidator);
        return TableGroupResponse.of(tableGroupRepository.save(tableGroup));
    }

    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> {
                    throw new CanNotUnGroupException(NOT_FOUND_TABLE_GROUP_ERROR_MESSAGE);
                });
        tableGroup.ungroup(tableGroupValidator);
        tableGroupRepository.deleteById(tableGroupId);
    }
}
