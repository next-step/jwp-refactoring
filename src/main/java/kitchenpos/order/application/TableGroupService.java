package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.domain.service.TableGroupDomainService;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;
    private final TableGroupDomainService tableGroupDomainService;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository, OrderRepository orderRepository,
                             final TableGroupDomainService tableGroupDomainService) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
        this.tableGroupDomainService = tableGroupDomainService;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = findOrderTableAllBy(tableGroupRequest.getOrderTables());
        verifyAvailableTableGroupSize(tableGroupRequest.getOrderTables().size(), orderTables.size());
        TableGroup group = tableGroupDomainService.group(orderTables);
        TableGroup saveTableGroup = tableGroupRepository.save(group);
        return TableGroupResponse.of(saveTableGroup, orderTables);
    }

    private List<OrderTable> findOrderTableAllBy(List<OrderTableRequest> orderTableRequests) {
        List<Long> orderTableIds = findOrderTableIds(orderTableRequests);
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        return orderTables;
    }

    private List<Long> findOrderTableIds(List<OrderTableRequest> orderTableRequests) {
        return orderTableRequests.stream()
                .map(orderTableRequest -> orderTableRequest.getId())
                .collect(Collectors.toList());
    }

    private void verifyAvailableTableGroupSize(int requestSize, int dbSize) {
        if (requestSize != dbSize) {
            throw new IllegalArgumentException("요청한 단체지정의 주문테이블 수와 디비의 주문테이블 수가 불일치합니다.");
        }
    }

    public void ungroup(final Long tableGroupId) {
        List<Order> orders = orderRepository.findAllByTableGroupId(tableGroupId);
        List<OrderTable> orderTables = orders.stream()
                .map(Order::getOrderTable)
                .collect(Collectors.toList());
        tableGroupDomainService.ungroup(orderTables, orders);
    }
}
