package kitchenpos.table.domain;

import static common.OrderTableFixture.단체지정_두번째_계산완료;
import static common.OrderTableFixture.단체지정_두번째_주문테이블;
import static common.OrderTableFixture.단체지정_첫번째_계산완료;
import static common.OrderTableFixture.단체지정_첫번째_주문테이블;
import static java.util.Arrays.asList;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TableGroupTest {

    @Test
    void 단체그룹_취소_예외() {
        // given
        OrderTables orderTables = OrderTables.of(asList(단체지정_첫번째_주문테이블(), 단체지정_두번째_주문테이블()));

        // then
        Assertions.assertThatThrownBy(orderTables::unGroup)
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체그룹_취소() {
        // given
        OrderTable 단체지정_첫번째_계산완료 = 단체지정_첫번째_계산완료();
        OrderTable 단체지정_두번째_계산완료 = 단체지정_두번째_계산완료();
        OrderTables orderTables = OrderTables.of(asList(단체지정_첫번째_계산완료, 단체지정_두번째_계산완료));

        // when
        orderTables.unGroup();

        // then
        Assertions.assertThat(단체지정_첫번째_계산완료.getTableGroupId()).isNull();
        Assertions.assertThat(단체지정_두번째_계산완료.getTableGroupId()).isNull();
    }

}
