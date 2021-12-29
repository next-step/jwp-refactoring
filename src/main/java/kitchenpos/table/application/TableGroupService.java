package kitchenpos.table.application;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final TableValidator tableValidator;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
        final TableValidator tableValidator,
        final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository) {
        this.tableValidator = tableValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {

        final List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();

        final OrderTables savedOrderTables = new OrderTables(
            orderTableRepository.findAllByIdIn(orderTableIds));

        if (!savedOrderTables.sameSizeAs(orderTableIds.size())) {
            throw new IllegalArgumentException("table 목록이 유효하지 않습니다. "
                + "존재하지 않는 테이블이 있거나, 목록이 unique 하지 않습니다.");
        }

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroupRequest.toEntity());
        savedOrderTables.toGroup(tableValidator, savedTableGroup);

        orderTableRepository.saveAll(savedOrderTables.getOrderTables());
        savedTableGroup.setOrderTables(savedOrderTables);

        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTableList = orderTableRepository.findByTableGroupId(tableGroupId);
        final OrderTables orderTables = new OrderTables(orderTableList);

        orderTables.ungroup(tableValidator);
    }

}
