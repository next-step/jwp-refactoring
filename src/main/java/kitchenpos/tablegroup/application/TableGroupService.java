package kitchenpos.tablegroup.application;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(
            TableGroupRepository tableGroupRepository
            , TableGroupValidator tableGroupValidator
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        TableGroup tableGroup = TableGroup.of(LocalDateTime.now());
        TableGroup persistTableGroup = tableGroupRepository.save(tableGroup);

        persistTableGroup.create(request.getOrderTableIds(), tableGroupValidator);
        return TableGroupResponse.of(persistTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findById(tableGroupId);
        tableGroup.ungroup(tableGroupValidator);
    }

    public TableGroup findById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(NoSuchElementException::new);
    }
}
