package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableGroupService {
    private static final String MORE_THAN_TWO_TABLE = "단체 지정을 위해서는 두 개 이상의 테이블이 필요합니다.";
    public static final String ALREADY_USE_ORDER_TABLE = "주문 테이블이 이미 사용중입니다.";

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTableRequest> orderTableRequests = tableGroupRequest.getOrderTables();

        validateOrderTableSize(orderTableRequests);

        final List<Long> orderTableIds = orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());

        final OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));
        savedOrderTables.validateOrderTables();
        savedOrderTables.validateOrderTableSize(orderTableRequests.size());

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), savedOrderTables));

        savedOrderTables.mappingTableGroup(savedTableGroup);
        orderTableRepository.saveAll(savedOrderTables.orderTables());
        savedTableGroup.mappingOrderTables(savedOrderTables);

        return TableGroupResponse.of(savedTableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));

        validateOrderTablesAlreadyUse(orderTables.orderTableIds());

        orderTables.unGroup();
        orderTableRepository.saveAll(orderTables.orderTables());
    }

    private void validateOrderTableSize(List<OrderTableRequest> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException(MORE_THAN_TWO_TABLE);
        }
    }

    private void validateOrderTablesAlreadyUse(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException(ALREADY_USE_ORDER_TABLE);
        }
    }
}
