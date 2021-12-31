package kitchenpos.tablegroup.application;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupDeleteEvent;
import kitchenpos.tablegroup.domain.TableGroupSaveEvent;
import kitchenpos.tablegroup.domain.TableGroupValidator;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.mapper.TableGroupMapper;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableGroupService(final TableGroupRepository tableGroupRepository, final TableGroupValidator tableGroupValidator, final ApplicationEventPublisher applicationEventPublisher) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        tableGroupValidator.createValidator(request);

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());
        final List<Long> orderTableIds = request.getOrderTables()
                .stream()
                .map(TableGroupCreateRequest.OrderTable::getId)
                .collect(Collectors.toList());

        applicationEventPublisher.publishEvent(new TableGroupSaveEvent(savedTableGroup.getId(), orderTableIds));

        return TableGroupMapper.toTableGroupResponse(savedTableGroup, request);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        tableGroupRepository.deleteById(tableGroupId);
        applicationEventPublisher.publishEvent(new TableGroupDeleteEvent(tableGroupId));
    }
}
