package kitchenpos.tableGroup.application;

import kitchenpos.table.application.TableService;
import kitchenpos.tableGroup.dao.TableGroupRepository;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.dto.TableGroupCreateRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
import kitchenpos.tableGroup.validator.TableGroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableService tableService;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(
            final TableGroupRepository tableGroupRepository,
            final TableService tableService,
            final TableGroupValidator tableGroupValidator
    ) {
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    public TableGroup getTableGroup(final Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 에 해당하는 단체 지정 정보를 찾을 수 없습니다."));
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        TableGroup tableGroup = request.of(tableService.findOrderTablesByIds(request.getOrderTables()));
        tableGroupValidator.validateTableGroup(tableGroup);

        return TableGroupResponse.from(
                tableGroupRepository.save(tableGroup)
        );
    }

    @Transactional
    public void ungroup(final Long id) {
        TableGroup tableGroup = getTableGroup(id);
        tableGroupValidator.possibleUngroupTableGroup(tableGroup);

        tableGroup.ungroup();
    }
}
