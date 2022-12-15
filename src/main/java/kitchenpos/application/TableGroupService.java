package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.OrderTable2;
import kitchenpos.domain.TableGroup2;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.ui.dto.TableGroupResponse;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableService tableService;
    private final OrderService orderService;

    public TableGroupService(TableGroupRepository tableGroupRepository,
                             TableService tableService,
                             OrderService orderService) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableService = tableService;
        this.orderService = orderService;
    }

    @Transactional
    public TableGroupResponse create(List<Long> orderTableId) {
        return new TableGroupResponse(save(orderTableId));
    }

    @Transactional
    public TableGroup2 save(List<Long> orderTableId) {
        List<OrderTable2> orderTable = tableService.findAllById(orderTableId);
        validateExistsAll(orderTableId, orderTable);

        TableGroup2 tableGroup = TableGroup2.create(orderTable);

        return tableGroupRepository.save(tableGroup);
    }

    private void validateExistsAll(List<Long> orderTableId, List<OrderTable2> orderTable) {
        if (orderTableId.size() != orderTable.size()) {
            throw new EntityNotFoundException();
        }
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup2 savedTableGroup = findById(tableGroupId);
        // TODO order should not exists in cookcing or meal

        savedTableGroup.ungroup();

    }

    private TableGroup2 findById(Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
            .orElseThrow(EntityNotFoundException::new);
    }
}
