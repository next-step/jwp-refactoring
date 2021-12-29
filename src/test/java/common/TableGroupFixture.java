package common;

import static common.OrderTableFixture.단체지정_두번째_주문테이블;
import static common.OrderTableFixture.단체지정_첫번째_주문테이블;
import static java.util.Arrays.asList;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.tableGroup.domain.TableGroup;

public class TableGroupFixture {

    public static final long TABLE_GROUP_ID_1 = 1L;

    public static TableGroup 단체테이블_첫번째_두번째() {
        OrderTable 단체지정_첫번째_주문테이블 = 단체지정_첫번째_주문테이블();
        단체지정_첫번째_주문테이블.group(TABLE_GROUP_ID_1);

        OrderTable 단체지정_두번째_주문테이블 = 단체지정_두번째_주문테이블();
        단체지정_두번째_주문테이블.group(TABLE_GROUP_ID_1);

        return TableGroup.of(TABLE_GROUP_ID_1, asList(단체지정_첫번째_주문테이블, 단체지정_두번째_주문테이블));
    }

}
