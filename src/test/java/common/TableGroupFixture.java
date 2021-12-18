package common;

import static common.OrderTableFixture.단체지정_두번째_주문테이블;
import static common.OrderTableFixture.단체지정_첫번째_주문테이블;
import static java.util.Arrays.asList;

import java.time.LocalDateTime;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {

    public static TableGroup 단체테이블_첫번째_두번째() {
        return new TableGroup(1L, LocalDateTime.now(), asList(단체지정_첫번째_주문테이블(), 단체지정_두번째_주문테이블()));
    }

}
