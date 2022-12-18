package kitchenpos.domain;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.fixture.OrderTableTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderTablesTest {

    @DisplayName("주문테이블 목록 생성 작업을 성공한다.")
    @Test
    void of() {
        // given
        OrderTable 주문테이블1 = 그룹_없는_주문테이블_생성(주문테이블(null, null, 10, true));
        OrderTable 주문테이블2 = 그룹_없는_주문테이블_생성(주문테이블(null, null, 20, true));

        // when
        OrderTables orderTables = OrderTables.from(Arrays.asList(주문테이블1, 주문테이블2));

        // then
        assertAll(
                () -> assertThat(orderTables.value()).containsExactly(주문테이블1, 주문테이블2)
        );
    }

    @DisplayName("주문테이블 목록 생성할 때, 주문테이블이 2개 미만이면 IllegalArgumentException을 반환한다.")
    @Test
    void ofWithException1() {
        // given
        OrderTable 주문테이블1 = 그룹_없는_주문테이블_생성(주문테이블(null, null, 10, true));

        // when & then
        assertThatThrownBy(() -> OrderTables.from(Collections.singletonList(주문테이블1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 목록 생성할 때, 그룹이 지정되어 있으면 IllegalArgumentException을 반홚한다.")
    @Test
    void ofWithException2() {
        // given
        OrderTable 주문테이블1 = 그룹_있는_주문테이블_생성(주문테이블(null, 1L, 10, true));
        OrderTable 주문테이블2 = 그룹_없는_주문테이블_생성(주문테이블(null, null, 20, true));
        주문테이블1.group(1L);

        // when & then
        assertThatThrownBy(() -> OrderTables.from(Arrays.asList(주문테이블1, 주문테이블2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 목록 생성할 때, 테이블이 비어있지 않으면 IllegalArgumentException을 반홚한다.")
    @Test
    void ofWithException3() {
        // given
        OrderTable 주문테이블1 = 그룹_없는_주문테이블_생성(주문테이블(null, null, 10, false));
        OrderTable 주문테이블2 = 그룹_없는_주문테이블_생성(주문테이블(null, null, 20, true));

        // when & then
        assertThatThrownBy(() -> OrderTables.from(Arrays.asList(주문테이블1, 주문테이블2)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
