package kitchenpos.application.tablegroup;

import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.table.TableGroupUnGroupEvent;
import kitchenpos.dto.tablegroup.TableGroupCreatedEventRequest;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.domain.tablegroup.TableGroupCreatedEvent;
import kitchenpos.repository.tablegroup.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher publisher;


    public TableGroupService(TableGroupRepository tableGroupRepository, ApplicationEventPublisher publisher) {
        this.tableGroupRepository = tableGroupRepository;
        this.publisher = publisher;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {

        final List<Long> orderTableIds = tableGroupRequest.getOrderTables();

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        TableGroupCreatedEventRequest tableGroupCreatedEventRequest = new TableGroupCreatedEventRequest(tableGroup.getId(), orderTableIds);
        publisher.publishEvent(new TableGroupCreatedEvent(tableGroupCreatedEventRequest));

        return tableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {

        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                                                    .orElseThrow(() -> new IllegalArgumentException("해당 단체지정이 등록되어 있지 않습니다."));

        publisher.publishEvent(new TableGroupUnGroupEvent(tableGroup.getId()));

    }
}
