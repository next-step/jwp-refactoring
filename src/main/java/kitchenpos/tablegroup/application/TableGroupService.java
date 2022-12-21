package kitchenpos.tablegroup.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.error.ErrorEnum;
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

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public TableGroupService(TableGroupRepository tableGroupRepository, final OrderRepository orderRepository, final OrderTableRepository orderTableRepository) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTable> savedOrderTables = findAllOrderTablesByIds(request.getOrderTableIds());
        final TableGroup savedTableGroup = tableGroupRepository.save(request.createTableGroup(savedOrderTables));
        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = findTableGroupById(tableGroupId);
        List<Order> orders = findAllOrderByTableIds(tableGroup.getOrderTableIds());

        tableGroup.ungroup(orders);
        tableGroupRepository.save(tableGroup);
    }

    private List<OrderTable> findAllOrderTablesByIds(List<Long> ids) {
        return ids.stream()
                .map(this::findOrderTableById)
                .collect(Collectors.toList());
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorEnum.NOT_EXISTS_ORDER_TABLE.message()));
    }

    private List<Order> findAllOrderByTableIds(List<Long> ids) {
        return orderRepository.findAllByOrderTableIdIn(ids);
    }
    private TableGroup findTableGroupById(Long id) {
        return tableGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ErrorEnum.NOT_EXISTS_TABLE_GROUP.message()));
    }
}
