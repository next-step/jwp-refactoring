package kitchenpos.table.application;

import kitchenpos.order.domain.order.OrderRepository;
import kitchenpos.table.domain.table.OrderTable;
import kitchenpos.table.domain.table.OrderTableRepository;
import kitchenpos.table.domain.tablegroup.TableGroup;
import kitchenpos.table.domain.tablegroup.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse saveTableGroup(final TableGroupRequest tableGroupRequest) {
        final TableGroup saveTableGroup = new TableGroup();

        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();

        for (final Long orderTableId : orderTableIds) {
            OrderTable findOrderTable = orderTableRepository.findById(orderTableId)
                    .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

            saveTableGroup.addOrderTable(findOrderTable);
        }

        return TableGroupResponse.of(tableGroupRepository.save(saveTableGroup));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup savedTableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("테이블 그룹이 존재하지 않습니다."));

        savedTableGroup.ungroup();

        tableGroupRepository.delete(savedTableGroup);
    }
}
