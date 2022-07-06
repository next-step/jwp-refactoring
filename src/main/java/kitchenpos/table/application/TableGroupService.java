package kitchenpos.table.application;

import java.util.List;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    private final OrderService orderService;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository, final OrderService orderService) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderService = orderService;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTable> savedOrderTables = findOrderTablesByIds(request);

        OrderTables orderTables = new OrderTables(savedOrderTables);

        TableGroup tableGroup = new TableGroup();
        tableGroup.group(orderTables);

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return TableGroupResponse.from(savedTableGroup);
    }

    private List<OrderTable> findOrderTablesByIds(TableGroupRequest request) {
        final List<Long> orderTableIds = request.getOrderTableIds();
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블을 포함하고 있어 단체 지정할 수 없습니다.");
        }
        return savedOrderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = findTableGroupById(tableGroupId);
        OrderTables orderTables = tableGroup.getOrderTables();
        if (orderService.existOrderBeforeCompletion(orderTables.value())) {
            throw new IllegalArgumentException("계산 완료되지 않은 테이블이 있어 단체 지정할 수 없습니다.");
        }
        tableGroup.ungroup();
    }

    private TableGroup findTableGroupById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId).orElseThrow(
                () -> new IllegalStateException(String.format("입력한 아이디(%d)의 단체 지정을 찾을 수 없습니다.", tableGroupId)));
    }
}
