package kitchenpos.table.tablegroup.application;

import kitchenpos.table.tablegroup.domain.TableGroup;
import kitchenpos.table.tablegroup.domain.TableGroupCreateValidator;
import kitchenpos.table.tablegroup.domain.TableGroupUnGroupValidator;
import kitchenpos.table.tablegroup.event.AllocatedGroupEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kitchenpos.table.tablegroup.domain.TableGroupRepository;
import kitchenpos.table.tablegroup.dto.CreateTableGroupRequest;
import kitchenpos.table.tablegroup.event.DeAllocatedGroupEvent;
import kitchenpos.table.tablegroup.event.TableGroupEventPublisher;

import java.time.LocalDateTime;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupCreateValidator createValidator;
    private final TableGroupUnGroupValidator unGroupValidator;
    private final TableGroupEventPublisher tableGroupEventPublisher;

    public TableGroupService(TableGroupRepository tableGroupRepository, TableGroupCreateValidator createValidator,
            TableGroupUnGroupValidator unGroupValidator, TableGroupEventPublisher tableGroupEventPublisher) {
        this.tableGroupRepository = tableGroupRepository;
        this.createValidator = createValidator;
        this.unGroupValidator = unGroupValidator;
        this.tableGroupEventPublisher = tableGroupEventPublisher;
    }

    @Transactional
    public TableGroup create(final CreateTableGroupRequest request) {
        createValidator.validate(request);
        final TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.from(LocalDateTime.now()));
        tableGroupEventPublisher.publish(
                new AllocatedGroupEvent(this, request.getOrderTableIds(), savedTableGroup.getId()));
        return savedTableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup savedTableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("단체 지정 테이블을 찾을 수 없습니다"));
        unGroupValidator.validate(savedTableGroup.getId());
        tableGroupEventPublisher.publish(new DeAllocatedGroupEvent(this, savedTableGroup.getId()));
        tableGroupRepository.delete(savedTableGroup);
    }
}
