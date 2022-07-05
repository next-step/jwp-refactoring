package kitchenpos.orders.tablegroup.application;

import java.util.NoSuchElementException;
import kitchenpos.orders.order.event.TableGroupEvent;
import kitchenpos.orders.order.event.TableUngroupEvent;
import kitchenpos.orders.tablegroup.domain.TableGroup;
import kitchenpos.orders.tablegroup.domain.TableGroupRepository;
import kitchenpos.orders.tablegroup.dto.TableGroupRequest;
import kitchenpos.orders.tablegroup.dto.TableGroupResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final ApplicationEventPublisher eventPublisher;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(ApplicationEventPublisher eventPublisher, TableGroupRepository tableGroupRepository) {
        this.eventPublisher = eventPublisher;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());
        eventPublisher.publishEvent(new TableGroupEvent(savedTableGroup.getId(), request.getOrderTableIds()));
        return TableGroupResponse.of(savedTableGroup);
    }


    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup findTableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NoSuchElementException("해당 테이블 그룹이 존재 하지 않습니다."));
        eventPublisher.publishEvent(new TableUngroupEvent(findTableGroup.getId()));
    }

}
