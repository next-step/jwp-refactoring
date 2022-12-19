package kitchenpos.application;

import java.util.List;
import kitchenpos.application.validator.TableGroupValidator;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {
    private final TableGroupValidator tableGroupValidator;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
            final TableGroupValidator tableGroupValidator,
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository
    ) {
        this.tableGroupValidator = tableGroupValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(tableGroupRequest.getOrderTableIds());
        tableGroupValidator.validateCreate(tableGroupRequest.getOrderTableIds(), savedOrderTables);

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(savedOrderTables));

        return TableGroupResponse.of(savedTableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        final OrderTables orderTables = OrderTables.of(orderTableRepository.findAllByTableGroupId(tableGroupId));
        tableGroupValidator.existsByCookingAndMeal(orderTables.getOrderTableIds());
        orderTables.ungroup();
    }
}
