package kitchenpos.table.domain;

import kitchenpos.table.application.TableService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableGroupCreateValidator {

    private final TableService tableService;

    public TableGroupCreateValidator(TableService tableService) {
        this.tableService = tableService;
    }

    public void validate(TableGroup tableGroup) {
        final List<OrderTable> savedOrderTables = tableService.findAllByIdIn(tableGroup.tableIds());
        checkedAlreadyExistGroup(savedOrderTables);
        checkedTableSize(savedOrderTables, tableGroup.orderTables());
    }

    private void checkedAlreadyExistGroup(List<OrderTable> savedOrderTables) {
        if (groupIdAlreadyExist(savedOrderTables)) {
            throw new IllegalArgumentException("이미 단체 지정 된 주문 테이블이 존재합니다");
        }
    }

    private boolean groupIdAlreadyExist(List<OrderTable> savedOrderTables) {
        return savedOrderTables.stream().anyMatch(it -> it.getTableGroupId() != null);
    }

    private void checkedTableSize(List<OrderTable> savedOrderTables, List<OrderTable> orderTables) {
        if (savedOrderTables.size() != orderTables.size()) {
            throw new IllegalArgumentException("실제 저장 된 테이블 목록의 수와 요청한 테이블 목록의 수가 다릅니다");
        }
    }
}
