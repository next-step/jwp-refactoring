package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
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

        TableGroup saveTableGroup = tableGroupRepository.save(new TableGroup(OrderTables.of(savedOrderTables)));
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
