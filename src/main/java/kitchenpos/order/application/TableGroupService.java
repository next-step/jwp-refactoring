package kitchenpos.order.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.order.domain.OrderTables;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.order.dao.OrderTableRepository;
import kitchenpos.order.dao.TableGroupRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.order.domain.TableGroup;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
public class TableGroupService {
    private final OrderDao orderDao;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderDao orderDao, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderDao = orderDao;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final OrderTables savedOrderTables = new OrderTables(
            orderTableRepository.findAllByIdIn(tableGroupRequest.orderTableIds())
        );

        if (!savedOrderTables.sameSizeWith(tableGroupRequest.orderTableSize())) {
            throw new IllegalArgumentException("등록되지 않은 테이블이 포함되어 있습니다.");
        }

        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        tableGroup.updateOrderTables(savedOrderTables);

        return TableGroupResponse.of(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 단체 입니다."));

        if (orderDao.existsByOrderTableIdInAndOrderStatusIn(
            tableGroup.orderTableIds(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))) {
            throw new IllegalArgumentException("주문 상태가 조리중이거나 식사중인 테이블의 단체 지정은 해지할 수 없습니다.");
        }

        tableGroup.unGroup();
        tableGroupRepository.save(tableGroup);
    }
}
