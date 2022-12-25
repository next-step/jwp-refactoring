package kitchenpos.table.application;

import kitchenpos.table.dao.TableGroupRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.event.TableGroupCreateEvent;
import kitchenpos.table.validator.TableGroupValidator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator validator;
    private final ApplicationEventPublisher eventPublisher;

    public TableGroupService(TableGroupRepository tableGroupRepository,
        TableGroupValidator validator,
        ApplicationEventPublisher eventPublisher) {
        this.tableGroupRepository = tableGroupRepository;
        this.validator = validator;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        validator.validateTableGroup(request);
        TableGroup savedTableGroup = tableGroupRepository.save(request.toTableGroup());
        eventPublisher.publishEvent(
            new TableGroupCreateEvent(request.getOrderTableIds(), savedTableGroup.getId()));
        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블 그룹 입니다."));
        validator.validateUnGroup(tableGroup);
        tableGroup.ungroup();
        tableGroupRepository.save(tableGroup); //이벤트 발행
    }
}
