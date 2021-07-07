package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderRepository orderRepository;

    public TableGroupService(final OrderDao orderDao, final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository, OrderRepository orderRepository) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderRepository = orderRepository;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(tableGroupRequest.getIds());
        verifyAvailable(tableGroupRequest, orderTables);
        TableGroup saveTableGroup = tableGroupRepository.save(new TableGroup(orderTables));
        return TableGroupResponse.of(saveTableGroup);
    }

    private void verifyAvailable(TableGroupRequest tableGroupRequest, List<OrderTable> orderTables) {
        if (orderTables.size() != tableGroupRequest.getOrderTables().size()) {
            throw new IllegalArgumentException();
        }
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(IllegalArgumentException::new);
        final List<Long> orderTableIds = getOrderTableIds(tableGroup);
        verifyOrderStatus(orderTableIds);
        tableGroup.ungroup();
    }

    private void verifyOrderStatus(List<Long> orderTableIds) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
    }

    private List<Long> getOrderTableIds(TableGroup tableGroup) {
        return tableGroup.getOrderTables().stream()
                .map(orderTable -> orderTable.getId())
                .collect(Collectors.toList());
    }
}
