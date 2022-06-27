package kitchenpos.tablegroup.fixture;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.table.fixture.TableFixture.단체_주문_테이블_4명;
import static kitchenpos.table.fixture.TableFixture.단체_주문_테이블_6명;

public class TableGroupFixture {

    public static TableGroup 단체_주문_테이블_그룹 = create(1L, LocalDateTime.now(), Arrays.asList(단체_주문_테이블_4명, 단체_주문_테이블_6명));

    public static TableGroup create(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(orderTables);

        return tableGroup;
    }
}
