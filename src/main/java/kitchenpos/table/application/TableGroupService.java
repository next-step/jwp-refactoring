package kitchenpos.table.application;

import kitchenpos.exception.TableGroupException;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.publisher.TableEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

    public static final String NOT_FOUND_TABLE_GROUP_ERROR_MESSAGE = "테이블 그룹이 존재하지 않습니다.";

    private final TableGroupRepository tableGroupRepository;
    private final TableEventPublisher tableEventPublisher;

    public TableGroupService(TableGroupRepository tableGroupRepository, TableEventPublisher tableEventPublisher) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableEventPublisher = tableEventPublisher;
    }

    public TableGroupResponse create(final TableGroupRequest request) {
        TableGroup tableGroup = tableGroupRepository.save(request.toTableGroup());
        tableEventPublisher.groupEventPublish(tableGroup, request.getOrderTableIds());
        return TableGroupResponse.of(tableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new TableGroupException(NOT_FOUND_TABLE_GROUP_ERROR_MESSAGE));
        tableEventPublisher.ungroupEventPublish(tableGroup);
    }


}
