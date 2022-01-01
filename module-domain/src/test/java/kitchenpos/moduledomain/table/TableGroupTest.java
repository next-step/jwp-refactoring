package kitchenpos.moduledomain.table;

import static java.util.Arrays.asList;
import static kitchenpos.moduledomain.common.OrderTableFixture.단체지정_두번째_계산완료;
import static kitchenpos.moduledomain.common.OrderTableFixture.단체지정_두번째_주문테이블;
import static kitchenpos.moduledomain.common.OrderTableFixture.단체지정_첫번째_계산완료;
import static kitchenpos.moduledomain.common.OrderTableFixture.단체지정_첫번째_주문테이블;
import static kitchenpos.moduledomain.common.OrderTableFixture.첫번째_주문테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
