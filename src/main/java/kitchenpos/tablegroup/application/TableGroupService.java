package kitchenpos.tablegroup.application;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.order.domain.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository,
            OrderRepository orderRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest request) {
        List<OrderTable> savedOrderTables = findAllOrderTablesByIds(request.getOrderTableIds());

        TableGroup savedTableGroup = tableGroupRepository.save(request.createTableGroup(savedOrderTables));
        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = findTableGroupById(tableGroupId);
        List<Order> orders = findAllOrderByTableIds(tableGroup.getOrderTableIds());

        tableGroup.ungroup(orders);
        tableGroupRepository.save(tableGroup);
    }

    private List<OrderTable> findAllOrderTablesByIds(List<Long> ids) {
        List<OrderTable> orderTables = orderTableRepository.findAllById(ids);

        if (orderTables.size() != ids.size()) {
            throw new IllegalArgumentException(ErrorCode.ORDER_TABLE_IS_NOT_EXIST.getMessage());
        }

        return orderTables;
    }

    private List<Order> findAllOrderByTableIds(List<Long> ids) {
        return orderRepository.findAllByOrderTableIdIn(ids);
    }

    private TableGroup findTableGroupById(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.TABLE_GROUP_IS_NOT_EXIST.getMessage()));
    }
}
