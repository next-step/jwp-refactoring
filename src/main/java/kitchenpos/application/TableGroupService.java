package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {

        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();

        final OrderTables savedOrderTables = new OrderTables(
            orderTableRepository.findAllByIdIn(orderTableIds));

        if (!savedOrderTables.sameSizeAs(orderTableIds.size())) {
            throw new IllegalArgumentException("table 목록이 유효하지 않습니다. "
                + "존재하지 않는 테이블이 있거나, 목록이 unique 하지 않습니다.");
        }

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroupRequest.toEntity());
        savedOrderTables.toGroup(savedTableGroup);

        orderTableRepository.saveAll(savedOrderTables.getOrderTables());
        savedTableGroup.setOrderTables(savedOrderTables);

        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTableList = orderTableRepository.findByTableGroupId(tableGroupId);
        final OrderTables orderTables = new OrderTables(orderTableList);

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
            orderTables.getOrderTableIds(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }

        orderTables.ungroup();
    }

}
