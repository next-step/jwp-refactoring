package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import org.assertj.core.util.Lists;

import java.util.List;

import static kitchenpos.fixture.OrderTableFixture.orderTable1;
import static kitchenpos.fixture.OrderTableFixture.orderTable2;

public class OrderTablesFixture {
    public static List<OrderTable> orderTables = Lists.newArrayList(orderTable1, orderTable2);
}
