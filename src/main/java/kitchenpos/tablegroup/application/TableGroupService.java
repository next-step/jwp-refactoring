package kitchenpos.tablegroup.application;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupEvent;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableUngroupEvent;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.dto.TableGroupSaveRequest;
import kitchenpos.tablegroup.exception.TableGroupNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//TODO 트랜잭션 내에서의 이벤트를 사용할 때 주의할 점에 대해 고민해볼 것
@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;
    private final ApplicationEventPublisher eventPublisher;

    public TableGroupService(
            TableGroupRepository tableGroupRepository,
            TableGroupValidator tableGroupValidator,
            ApplicationEventPublisher eventPublisher
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupSaveRequest request) {
        tableGroupValidator.validateGroup(request.getTableIds());
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        eventPublisher.publishEvent(new TableGroupEvent(request.getTableIds(), tableGroup.getId()));
        return TableGroupResponse.of(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(TableGroupNotFoundException::new);
        tableGroupValidator.validateUngroup(tableGroup);
        tableGroupRepository.delete(tableGroup);
        eventPublisher.publishEvent(new TableUngroupEvent(tableGroup.getId()));
    }
}
