package kitchenpos.tableGroup.mapper;

import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.dto.TableGroupCreateRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TableGroupMapper {
    private static final int MINIMUM_COUNT = 2;

    private final TableService tableService;

    public TableGroupMapper(TableService tableService) {
        this.tableService = tableService;
    }

    public TableGroup mapFrom(TableGroupCreateRequest request) {
        OrderTables orderTables = tableService.findOrderTablesByIds(request.getOrderTables());
        validateTableGroup(orderTables, request);

        return request.of(orderTables);
    }

    private void validateTableGroup(OrderTables orderTables, TableGroupCreateRequest request) {
        if (orderTables == null) {
            throw new IllegalArgumentException("단체 지정이 요청된 주문 테이블이 존재하지 않습니다.");
        }
        if (orderTables.getValue().size() != request.getOrderTables().size()) {
            throw new IllegalArgumentException("생성 요청된 단체 지정에 존재하지 않는 주문 테이블이 있습니다.");
        }
        if (orderTables.getValue().size() < MINIMUM_COUNT) {
            throw new IllegalArgumentException("단체 지정에 속한 주문 테이블의 수는 최소 2개 이상이어야 합니다.");
        }

        orderTables.getValue().forEach(this::checkPossibleBelongGroup);
    }

    private void checkPossibleBelongGroup(OrderTable orderTable) {
        if (orderTable.isNotEmpty()) {
            throw new IllegalArgumentException("빈 주문 테이블만 새로운 그룹에 속할 수 있습니다.");
        }
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException("이미 단체 지정에 속한 주문 테이블이 존재합니다.");
        }
    }
}
