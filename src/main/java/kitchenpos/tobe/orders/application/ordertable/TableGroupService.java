package kitchenpos.tobe.orders.application.ordertable;

import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.tobe.common.domain.Validator;
import kitchenpos.tobe.orders.domain.ordertable.OrderTable;
import kitchenpos.tobe.orders.domain.ordertable.OrderTableRepository;
import kitchenpos.tobe.orders.domain.ordertable.OrderTables;
import kitchenpos.tobe.orders.domain.ordertable.TableGroup;
import kitchenpos.tobe.orders.domain.ordertable.TableGroupRepository;
import kitchenpos.tobe.orders.dto.ordertable.TableGroupRequest;
import kitchenpos.tobe.orders.dto.ordertable.TableGroupResponse;
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
