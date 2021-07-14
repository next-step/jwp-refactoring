package kitchenpos.tablegroup.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.event.GroupedTablesEvent;
import kitchenpos.common.event.UngroupedTablesEvent;
import kitchenpos.order.application.OrderValidator;
import kitchenpos.table.application.OrderTableValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.MisMatchedOrderTablesSizeException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@Service
@Transactional
public class TableGroupService {
    private static final int ORDER_TABLE_MINIMUM_SIZE = 2;
    private final ApplicationEventPublisher publisher;
    private final OrderValidator orderValidator;
    private final OrderTableValidator orderTableValidator;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupService(final ApplicationEventPublisher publisher, final OrderValidator orderValidator,
                             final OrderTableValidator orderTableValidator, final TableGroupRepository tableGroupRepository,
                             final OrderTableRepository orderTableRepository) {
        this.publisher = publisher;
        this.orderValidator = orderValidator;
        this.orderTableValidator = orderTableValidator;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void ungroup(final Long tableGroupId) {
        List<Long> orderTableIds = orderTableRepository.findByTableGroupId(tableGroupId)
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        orderValidator.validateExistsOrdersStatusIsCookingOrMeal(orderTableIds);
        publisher.publishEvent(new UngroupedTablesEvent(orderTableIds));
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        validateOrderTableSize(orderTableRepository.countByIdIn(orderTableIds), orderTableIds.size());
        orderTableValidator.validateOrderTableIsEmptyOrHasTableGroups(orderTableIds);
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        publisher.publishEvent(new GroupedTablesEvent(orderTableIds, tableGroup.getId()));
        return TableGroupResponse.of(tableGroup);
    }

    private void validateOrderTableSize(Long findOrderTableSize, int orderTableIdsSize) {
        if (orderTableIdsSize < ORDER_TABLE_MINIMUM_SIZE) {
            throw new IllegalArgumentException("정산 그룹 생성은 2개 이상의 테이블만 가능합니다.");
        }
        if (findOrderTableSize != orderTableIdsSize) {
            throw new MisMatchedOrderTablesSizeException("입력된 항목과 조회결과가 일치하지 않습니다.");
        }
    }
}
