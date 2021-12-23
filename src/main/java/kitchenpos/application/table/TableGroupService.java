package kitchenpos.application.table;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.dto.table.TableGroupRequest;
import kitchenpos.event.TableGroupCreatedEvent;
import kitchenpos.event.TableGroupUnGroupEvent;
import kitchenpos.repository.table.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableService tableService;

    private final ApplicationEventPublisher publisher;


    public TableGroupService(TableGroupRepository tableGroupRepository, TableService tableService, ApplicationEventPublisher publisher) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableService = tableService;
        this.publisher = publisher;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroupRequest) {

        final List<Long> orderTableIds = tableGroupRequest.getOrderTables();
        List<OrderTable> orderTables = orderTableIds.stream()
                                                    .map(tableService::getOrderTable)
                                                    .collect(Collectors.toList());

        TableGroup tableGroup = new TableGroup(orderTables);
        publisher.publishEvent(new TableGroupCreatedEvent(tableGroup.getOrderTables()));

        return tableGroupRepository.save(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {

        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                                                    .orElseThrow(() -> new IllegalArgumentException("해당 단체지정이 등록되어 있지 않습니다."));

        final List<OrderTable> orderTables = tableService.getOrderTablesByTableGroup(tableGroup);
        publisher.publishEvent(new TableGroupUnGroupEvent(orderTables));

    }
}
