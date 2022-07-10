package kitchenpos.table.application;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Transactional
@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupRequest request) {
        final TableGroup tableGroup = request.toEntity();
        final OrderTables orderTables = tableGroup.getOrderTables();
        final List<Long> orderTableIds = orderTables.extractIds();
        final OrderTables savedOrderTables = new OrderTables(orderTableRepository.findAllByIdIn(orderTableIds));

        tableGroup.changeCreatedDate(LocalDateTime.now(), savedOrderTables, orderTables.getSize());

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return TableGroupResponse.of(savedTableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.TABLE_GROUP_NOT_FOUND));

        tableGroup.unGroup();
        tableGroupRepository.save(tableGroup);
    }
}
