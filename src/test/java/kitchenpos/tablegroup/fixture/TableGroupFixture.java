package kitchenpos.tablegroup.fixture;

import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.table.fixture.TableFixture.단체_주문_테이블_4명;
import static kitchenpos.table.fixture.TableFixture.단체_주문_테이블_6명;

public class TableGroupFixture {

    public static TableGroup 단체_주문_테이블_그룹 = create(
            1L,
            LocalDateTime.now(),
            OrderTables.of(Arrays.asList(단체_주문_테이블_4명, 단체_주문_테이블_6명))
    );

    public static TableGroup create(Long id, LocalDateTime createdDate, OrderTables orderTables) {
        return TableGroup.of(id, createdDate, orderTables);
    }
}
