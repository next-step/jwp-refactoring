package kitchenpos.table.application;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository, final OrderRepository orderRepository,
                             final TableGroupRepository tableGroupRepository) {

        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
        this.tableGroupRepository = tableGroupRepository;
    }


    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<Long> requestOrderTables = tableGroupRequest.getOrderTables();
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(requestOrderTables);

        validateExistOrderTable(requestOrderTables, savedOrderTables);

        TableGroup saveTableGroup = tableGroupRepository.save(new TableGroup(OrderTables.from(savedOrderTables)));
        saveTableGroup.group();

        return new TableGroupResponse(saveTableGroup);
    }

    private void validateExistOrderTable(List<Long> requestOrderTables, List<OrderTable> savedOrderTables) {
        if (requestOrderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("단체지정할 주문 테이블이 없으면 단체석으로 지정할 수 없습니다.");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NoSuchElementException("해당 단체지정석을 찾을수 없습니다."));

        validateUngroup(tableGroup);

        tableGroup.unGroup();
    }

    private void validateUngroup(TableGroup tableGroup) {
        final List<Long> orderTableIds = tableGroup.getOrderTables()
                .stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException("주문 테이블중 조리중인 경우에 단체석을 개인 주문테이블로 변경할 수 없습니다.");
        }
    }
}
