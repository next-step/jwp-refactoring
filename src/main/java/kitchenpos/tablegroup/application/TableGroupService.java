package kitchenpos.tablegroup.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupMapper;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableGroupValidator;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.event.TableGroupUpdateEvent;
import kitchenpos.tablegroup.event.TableUnGroupEvent;

@Transactional
@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;
    private final ApplicationEventPublisher publisher;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
        final TableGroupValidator tableGroupValidator,
        final ApplicationEventPublisher publisher) {

        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
        this.publisher = publisher;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final TableGroupMapper tableGroupMapper = TableGroupMapper.of(tableGroupValidator, tableGroupRequest);
        final TableGroup tableGroup = tableGroupMapper.toTableGroup();
        final TableGroup saved = tableGroupRepository.save(tableGroup);
        final TableGroupUpdateEvent tableGroupUpdateEvent = tableGroupMapper.toTableGroupUpdateEvent(saved);
        publisher.publishEvent(tableGroupUpdateEvent);

        return TableGroupResponse.of(saved);
    }

    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findTableGroup(tableGroupId);
        publisher.publishEvent(new TableUnGroupEvent(tableGroup));
    }

    private TableGroup findTableGroup(final Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
            .orElseThrow(IllegalArgumentException::new);
    }
}
