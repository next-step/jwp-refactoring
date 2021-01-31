package kitchenpos.tablegroup.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.table.OrderTableRepository;
import kitchenpos.table.OrderTable;
import kitchenpos.table.TableService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupAddRequest;
import kitchenpos.tablegroup.dto.TableGroupMapper;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TableGroupService {

    private final TableGroupMapper mapper = Mappers.getMapper(TableGroupMapper.class);

    private final TableService tableService;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(TableService tableService, TableGroupRepository tableGroupRepository) {
        this.tableService = tableService;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupAddRequest request) {
        final List<OrderTable> orderTables = request.getOrderTables().stream()
                .map(it -> tableService.getOne(it.getId()))
                .collect(Collectors.toList());
        request.checkSameSize(orderTables.size());
        return mapper.toResponse(tableGroupRepository.save(new TableGroup(orderTables)));
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.getOne(tableGroupId);
        final List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        if (tableService.existsByOrderTable_IdInAndOrderStatusIn(orderTableIds, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalArgumentException();
        }
        tableGroup.ungroup();
    }

}
