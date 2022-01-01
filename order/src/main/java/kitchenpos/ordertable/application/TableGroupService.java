package kitchenpos.ordertable.application;

import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.domain.Validator;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTables;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.ordertable.domain.TableGroupRepository;
import kitchenpos.ordertable.dto.TableGroupRequest;
import kitchenpos.ordertable.dto.TableGroupResponse;
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
