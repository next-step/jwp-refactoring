package kitchenpos.table.application;

import kitchenpos.order.application.OrderTableService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableService orderTableService;

    public TableGroupService(final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             OrderTableService orderTableService) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableService = orderTableService;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        checkValidation(request);
        TableGroup tableGroup = new TableGroup(findOrderTables(request));
        TableGroup save = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(save);
    }

    private void checkValidation(TableGroupRequest request) {
        if (request.getOrderTables().isEmpty() || request.getOrderTables().size() < 2) {
            throw new IllegalArgumentException("주문 테이블이 2개 이상이어야 합니다.");
        }

        List<OrderTable> savedOrderTables = findOrderTables(request);
        if (request.getOrderTables().size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문테이블을 포함합니다.");
        }

        if (savedOrderTables.stream().anyMatch(OrderTable::inGroup)) {
            throw new IllegalArgumentException("단체 지정은 중복될 수 없습니다.");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        checkOrderStatus(orderTableIds);
        orderTables.forEach(OrderTable::ungroup);
    }

    private void checkOrderStatus(List<Long> orderTableIds) {
        orderRepository.findByOrderTableIdIn(orderTableIds)
                .stream()
                .filter(it -> it.statusIsCooking() || it.statusIsMeal())
                .findAny()
                .ifPresent(it -> {throw new IllegalArgumentException("요리중이거나 식사중인 테이블은 변경할 수 없습니다.");});
    }

    private List<OrderTable> findOrderTables(final TableGroupRequest request) {
        return request.getOrderTables()
                .stream()
                .map(OrderTableRequest::getId)
                .map(orderTableService::findById)
                .collect(Collectors.toList());
    }
}
