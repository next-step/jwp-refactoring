package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableIdRequest;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.exception.KitchenposErrorCode;
import kitchenpos.exception.KitchenposException;
import kitchenpos.exception.KitchenposNotFoundException;

@Service
@Transactional(readOnly = true)
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        request.checkValidSize();
        final List<Long> orderTableIds = request.getOrderTables().stream()
            .map(OrderTableIdRequest::getId)
            .collect(Collectors.toList());

        TableGroup tableGroup = makeTableGroup(orderTableIds);
        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    private TableGroup makeTableGroup(List<Long> orderTableIds) {
        final OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));
        savedOrderTables.checkSameSize(orderTableIds.size());
        savedOrderTables.checkNotContainsUsedTable();

        TableGroup tableGroup = new TableGroup();
        tableGroup.addOrderTables(savedOrderTables);

        return tableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(KitchenposNotFoundException::new);
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroup(tableGroup));

        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
            orderTables.getOrderTables(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new KitchenposException(KitchenposErrorCode.CONTAINS_USED_TABLE);
        }

        orderTables.unGroup();
    }
}
