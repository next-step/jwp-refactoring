package kitchenpos.application;

import static java.util.Arrays.*;
import static kitchenpos.domain.OrderStatus.*;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
        final OrderTables orderTables = findOrderTables(tableGroupRequest);
        final TableGroup persistTableGroup = tableGroupRepository.save(new TableGroup(orderTables, LocalDateTime.now()));
        return TableGroupResponse.of(persistTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException("등록된 테이블 그룹만 그룹해제 가능합니다."));
        validateNotExistsCookingAndMeal(tableGroup);
        tableGroup.ungroup();
    }

    private void validateNotExistsCookingAndMeal(TableGroup tableGroup) {
        final List<Long> orderTableIds = tableGroup.getOrderTableIds();
        // if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, asList(COOKING, MEAL))) {
        //     throw new IllegalArgumentException("조리상태이거나 식사상태인 주문이 있는 주문테이블은 그룹해제를 할 수 없습니다.");
        // }
    }

    private OrderTables findOrderTables(final TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException("등록이 되지 않은 주문테이블은 그룹화 할 수 없습니다.");
        }
        return OrderTables.of(orderTables);
    }
}
