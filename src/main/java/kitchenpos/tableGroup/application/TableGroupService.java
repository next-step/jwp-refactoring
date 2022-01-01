package kitchenpos.tableGroup.application;

import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
import kitchenpos.tableGroup.event.GroupEvent;
import kitchenpos.tableGroup.event.UngroupEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public TableGroupService(final TableGroupRepository tableGroupRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.tableGroupRepository = tableGroupRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        validateCreateTableGroup(tableGroupRequest);

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());
        applicationEventPublisher.publishEvent(new GroupEvent(savedTableGroup.getId(), tableGroupRequest.toOrderTableIds()));

        return TableGroupResponse.of(savedTableGroup);
    }

    private void validateCreateTableGroup(TableGroupRequest tableGroupRequest) {
        if (tableGroupRequest.orderTablesSize() < 2) {
            throw new IllegalArgumentException("단체 지정을 하려면 주문 테이블을 2개 이상 선택해주세요.");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹 정보가 존재하지 않습니다."));
        applicationEventPublisher.publishEvent(new UngroupEvent(tableGroupId));
    }
}
