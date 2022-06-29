package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequestDto;
import kitchenpos.table.dto.TableGroupResponseDto;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class TableGroupService {

    private final TableValidator tableValidator;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(TableValidator tableValidator, OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.tableValidator = tableValidator;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponseDto create(final TableGroupRequestDto request) {
        List<Long> orderTableIds = request.getOrderTableIds();
        List<OrderTable> orderTables = orderTableRepository.findByIdIn(orderTableIds);
        checkNotFoundOrderTables(orderTableIds, orderTables);

        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
        tableGroup.group(orderTables);
        return new TableGroupResponseDto(tableGroup);
    }

    private void checkNotFoundOrderTables(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId).orElseThrow(EntityNotFoundException::new);
        tableValidator.checkValidUngroup(tableGroup);
        tableGroup.ungroup();
    }
}
