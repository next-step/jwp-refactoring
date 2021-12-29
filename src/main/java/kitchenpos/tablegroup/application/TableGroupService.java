package kitchenpos.tablegroup.application;

import kitchenpos.exception.NotExistEntityException;
import kitchenpos.table.application.TableGroupingEvent;
import kitchenpos.table.application.TableService;
import kitchenpos.table.application.TableUnGroupingEvent;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        tableGroup.grouping(tableGroupRequest.toOrderTableIds());
        TableGroup saveTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(saveTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {

        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NotExistEntityException("지정된 단체를 찾을 수 없습니다."));

        tableGroup.unGrouping();
    }
}
