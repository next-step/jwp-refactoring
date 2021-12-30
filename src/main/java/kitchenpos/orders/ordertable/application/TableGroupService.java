package kitchenpos.orders.ordertable.application;

import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.common.domain.Validator;
import kitchenpos.orders.ordertable.domain.OrderTable;
import kitchenpos.orders.ordertable.domain.OrderTableRepository;
import kitchenpos.orders.ordertable.domain.OrderTables;
import kitchenpos.orders.ordertable.domain.TableGroup;
import kitchenpos.orders.ordertable.domain.TableGroupRepository;
import kitchenpos.orders.ordertable.dto.TableGroupRequest;
import kitchenpos.orders.ordertable.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final Validator<TableGroup> validator;

    public TableGroupService(
        final TableGroupRepository tableGroupRepository,
        final OrderTableRepository orderTableRepository,
        final Validator<TableGroup> validator
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.validator = validator;
    }

    @Transactional
    public TableGroupResponse group(final TableGroupRequest request) {
        final TableGroup group = tableGroupRepository.save(request.toTableGroup());
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(
            request.getOrderTableIds()
        );
        group.group(new OrderTables(orderTables));
        return TableGroupResponse.of(group);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 단체를 해제할 수 없습니다."));
        tableGroup.ungroup(validator);
    }
}
