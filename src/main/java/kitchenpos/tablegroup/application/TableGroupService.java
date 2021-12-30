package kitchenpos.tablegroup.application;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.event.TableGroupedEvent;
import kitchenpos.tablegroup.domain.event.TableUnGroupedEvent;
import kitchenpos.ordertable.domain.validator.OrderTableCreateTableGroupValidator;
import kitchenpos.order.domain.validator.OrderUnGroupTableGroupValidator;
import kitchenpos.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.exception.CanNotUnGroupException;
import kitchenpos.tablegroup.infra.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {
    private static final String NOT_FOUND_TABLE_GROUP_ERROR_MESSAGE = "해당 단체 지정을 찾지 못하여 해산할 수 없습니다.";

    private final TableGroupRepository tableGroupRepository;
    private final OrderUnGroupTableGroupValidator orderUnGroupTableGroupValidator;
    private final OrderTableCreateTableGroupValidator orderTableCreateTableGroupValidator;
    private final ApplicationEventPublisher eventPublisher;

    public TableGroupService(TableGroupRepository tableGroupRepository,
                             OrderUnGroupTableGroupValidator orderUnGroupTableGroupValidator,
                             OrderTableCreateTableGroupValidator orderTableCreateTableGroupValidator,
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
