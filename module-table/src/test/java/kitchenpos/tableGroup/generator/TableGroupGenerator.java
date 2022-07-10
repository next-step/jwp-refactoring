package kitchenpos.tableGroup.generator;

import kitchenpos.table.domain.OrderTables;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.dto.TableGroupCreateRequest;

import java.util.List;

public class TableGroupGenerator {
    private static final String PATH = "/api/table-groups";

    public static TableGroup 테이블_그룹_생성(OrderTables orderTables) {
        return new TableGroup(orderTables);
    }

    public static TableGroupCreateRequest 테이블_그룹_생성_요청(List<Long> orderTableIds) {
        return new TableGroupCreateRequest(orderTableIds);
    }
}
