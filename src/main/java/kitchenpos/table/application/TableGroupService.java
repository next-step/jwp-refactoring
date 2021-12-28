package kitchenpos.table.application;

import kitchenpos.exception.NotExistEntityException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.apache.catalina.core.ApplicationPushBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher publisher;

    public TableGroupService(final TableService tableService, final TableGroupRepository tableGroupRepository,
                             final ApplicationEventPublisher publisher) {
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
        this.publisher = publisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final TableGroup tableGroup = tableGroupRequest.toEntity();
        TableGroup saveTableGroup = tableGroupRepository.save(tableGroup);
        publisher.publishEvent(new TableGroupingEvent(this, saveTableGroup.getId(), tableGroupRequest.toOrderTableIds()));
        return TableGroupResponse.of(saveTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {

        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NotExistEntityException("지정된 단체를 찾을 수 없습니다."));

        publisher.publishEvent(new TableUnGroupingEvent(this, tableGroupId));
    }
}
