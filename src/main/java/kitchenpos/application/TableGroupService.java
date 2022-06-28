package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.TableGroupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public TableGroupService(TableGroupRepository tableGroupRepository, OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest request) {
        validateIncorrectOrderTableSize(request.getOrderTables());

        // 주문 테이블 조회
        final List<Long> orderTableIds = request.getOrderTables()
                                                .stream()
                                                .map(OrderTableRequest::getId)
                                                .collect(Collectors.toList());

        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        validateOrderTableStatus(request.getOrderTables(), orderTables);

        return tableGroupRepository.save(new TableGroup(orderTables));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroup(tableGroupId);

        if (orderTables.size() == 0) {
            throw new NoSuchElementException("단체 지정된 주문 테이블이 존재하지 않습니다.");
        }

        List<Long> orderTableIds = orderTables.stream()
                                              .map(OrderTable::getId)
                                              .collect(Collectors.toList());

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문 상태가 조리 또는 식사인 테이블은 단체 지정 취소할 수 없습니다.");
        }

        orderTables.forEach(orderTable -> orderTable.changeTableGroup(null));
    }

    private void validateIncorrectOrderTableSize(List<OrderTableRequest> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableStatus(List<OrderTableRequest> request, List<OrderTable> orderTables) {
        if (request.size() != orderTables.size()) {
            throw new IllegalArgumentException("요청 테이블과 실제 테이블 개수가 일치하지 않습니다.");
        }

        orderTables.forEach(orderTable -> {
            if (!orderTable.isEmpty()) {
                throw new IllegalArgumentException("빈 테이블이 아니면 단체 지정할 수 없습니다.");
            }
            if (Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException("이미 단체 지정된 테이블 입니다.");
            }
        });
    }
}
