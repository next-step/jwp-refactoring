package common;

import static common.OrderTableFixture.단체지정_두번째_주문테이블;
import static common.OrderTableFixture.단체지정_첫번째_주문테이블;
import static java.util.Arrays.asList;

import java.time.LocalDateTime;
import kitchenpos.table.domain.TableGroup;

public class TableGroupFixture {

    public static TableGroup 단체테이블_첫번째_두번째() {
        return TableGroup.of(1L, asList(단체지정_첫번째_주문테이블(), 단체지정_두번째_주문테이블()));
    }

}
