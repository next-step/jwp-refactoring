package kitchenpos.validator.tablegroup;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.validator.TableGroupValidators;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class TableGroupValidatorsImpl implements TableGroupValidators {

    private final OrderTableRepository orderTableRepository;
    private final List<TableGroupValidator> tableGroupValidators;
    private final TablesGroupValidator tablesGroupValidator;
    private final TableUnGroupValidator tableUnGroupValidator;

    public TableGroupValidatorsImpl(OrderTableRepository orderTableRepository,
                                    List<TableGroupValidator> tableGroupValidators,
                                    TablesGroupValidator tablesGroupValidator,
                                    TableUnGroupValidator tableUnGroupValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupValidators = tableGroupValidators;
        this.tablesGroupValidator = tablesGroupValidator;
        this.tableUnGroupValidator = tableUnGroupValidator;
    }

    @Transactional(readOnly = true)
    public void validateCreation(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds)
                .orElseThrow(() -> new IllegalArgumentException("등록 된 주문 테이블에 대해서만 단체 지정이 가능합니다"));

        orderTables.forEach(orderTable -> tableGroupValidators
                .forEach(tableGroupValidator -> tableGroupValidator.validate(orderTable)));
        tablesGroupValidator.validate(orderTables);
    }

    @Transactional(readOnly = true)
    public void validateUngroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findListByTableGroupId(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("단체 지정을 해제할 주문 테이블이 없습니다"));

        tableUnGroupValidator.validate(orderTables);
    }
}
