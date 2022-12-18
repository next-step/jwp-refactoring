package kitchenpos.tablegroup.application;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.event.TableGroupedEvent;
import kitchenpos.tablegroup.event.TableUnGroupedEvent;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import kitchenpos.validator.tablegroup.TableGroupValidatorsImpl;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidatorsImpl tableGroupValidator;
    private final ApplicationEventPublisher eventPublisher;

    public TableGroupService(TableGroupRepository tableGroupRepository,
                             TableGroupValidatorsImpl tableGroupValidator,
                             ApplicationEventPublisher eventPublisher) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        tableGroupValidator.validateCreation(tableGroupRequest.getOrderTableIds());
        eventPublisher.publishEvent(new TableGroupedEvent(tableGroup.getId(), tableGroupRequest.getOrderTableIds()));
        return tableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "등록 되지 않은 단체 지정은 해제할 수 없습니다[tableGroupId:" + tableGroupId + "]"));

        tableGroupValidator.validateUngroup(tableGroupId);
        tableGroupRepository.delete(tableGroup);
        eventPublisher.publishEvent(new TableUnGroupedEvent(tableGroupId));
    }
}
