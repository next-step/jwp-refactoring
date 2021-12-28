package kitchenpos.application;

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
        final List<Long> orderTableIds = makeOrderTableIds(request);

        TableGroup tableGroup = makeTableGroup(orderTableIds);
        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    private List<Long> makeOrderTableIds(TableGroupRequest request) {
        return request.getOrderTables().stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }

    private TableGroup makeTableGroup(List<Long> orderTableIds) {
        final OrderTables orderTables = new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));
        orderTables.checkSameSize(orderTableIds.size());
        orderTables.checkNotContainsUsedTable();

        return new TableGroup(orderTables);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(KitchenposNotFoundException::new);
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroup(tableGroup));

        checkContainsCookingOrMealTable(orderTables);

        orderTables.unGroup();
    }

    private void checkContainsCookingOrMealTable(OrderTables orderTables) {
        if (orderRepository.existsByOrderTableInAndOrderStatusIn(
            orderTables.getOrderTables(), OrderStatus.NOT_COMPLETED_LIST)) {
            throw new KitchenposException(KitchenposErrorCode.CONTAINS_USED_TABLE);
        }
    }
}
