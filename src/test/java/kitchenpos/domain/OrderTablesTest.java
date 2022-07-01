package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTablesTest {

    @Test
    void 두개_미만의_테이블은_단체지정할_수_없다() {
        // given
        List<OrderTable> orderTables = Collections.singletonList(
                new OrderTable(1, true)
        );

        // when & then
        assertThatThrownBy(() ->
                new TableGroup(orderTables)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("2개 이상의 테이블을 단체 지정할 수 있습니다.");
    }
}
