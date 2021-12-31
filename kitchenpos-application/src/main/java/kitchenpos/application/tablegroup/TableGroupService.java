package kitchenpos.application.tablegroup;

import kitchenpos.domain.tablegroup.domain.TableGroup;
import kitchenpos.domain.tablegroup.domain.TableGroupRepository;
import kitchenpos.domain.tablegroup.exception.CanNotUnGroupException;
import kitchenpos.domain.order.validator.OrderTableGroupUnGroupValidator;
import kitchenpos.domain.ordertable.validator.OrderTableTableGroupCreateValidator;
import kitchenpos.domain.tablegroup.event.TableGroupedEvent;
import kitchenpos.domain.tablegroup.event.TableUnGroupedEvent;
import kitchenpos.domain.tablegroup.validator.TableGroupCreateValidator;
import kitchenpos.domain.tablegroup.validator.TableGroupUnGroupValidator;
import kitchenpos.application.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.application.tablegroup.dto.TableGroupResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {
    private static final String NOT_FOUND_TABLE_GROUP_ERROR_MESSAGE = "해당 단체 지정을 찾지 못하여 해산할 수 없습니다.";

    private final TableGroupRepository tableGroupRepository;
    private final TableGroupUnGroupValidator orderUnGroupTableGroupValidator;
    private final TableGroupCreateValidator orderTableCreateTableGroupValidator;
    private final ApplicationEventPublisher eventPublisher;

    public TableGroupService(TableGroupRepository tableGroupRepository,
                             OrderTableGroupUnGroupValidator orderUnGroupTableGroupValidator,
                             OrderTableTableGroupCreateValidator orderTableCreateTableGroupValidator,
                             ApplicationEventPublisher applicationEventPublisher) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderUnGroupTableGroupValidator = orderUnGroupTableGroupValidator;
        this.orderTableCreateTableGroupValidator = orderTableCreateTableGroupValidator;
        this.eventPublisher = applicationEventPublisher;
    }

    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final TableGroup tableGroup = TableGroup.create(request.getOrderTableIds(), orderTableCreateTableGroupValidator);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        eventPublisher.publishEvent(TableGroupedEvent.of(savedTableGroup));
        return TableGroupResponse.of(savedTableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> {
                    throw new CanNotUnGroupException(NOT_FOUND_TABLE_GROUP_ERROR_MESSAGE);
                });
        tableGroup.ungroup(orderUnGroupTableGroupValidator);
        tableGroupRepository.deleteById(tableGroupId);
        eventPublisher.publishEvent(TableUnGroupedEvent.of(tableGroup));
    }
}
