package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderTableTest {
    private TableGroup 단체_테이블;
    private OrderTable 단체_주문_테이블1;
    private OrderTable 단체_주문_테이블2;

    @BeforeEach
    void setUp() {
        단체_테이블 = new TableGroup();
        단체_주문_테이블1 = new OrderTable(0, true);
        단체_주문_테이블2 = new OrderTable(0, true);

        단체_테이블.group(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2));
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void 테이블_그룹_해제() {
        단체_주문_테이블1.ungroup();

        assertThat(단체_주문_테이블1.getTableGroupId()).isNull();
    }

    @DisplayName("테이블 그룹에 포함된 테이블은 빈 테이블로 변경할 수 없다.")
    @Test
    void 테이블_그룹에_포함된_테이블_빈_테이블_변경() {
        ReflectionTestUtils.setField(단체_주문_테이블1, "tableGroupId", 1L);

        assertThatThrownBy(() -> 단체_주문_테이블1.changeEmpty(true, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("요리중, 식사중인 테이블은 빈 테이블로 변경할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = { "COOKING", "MEAL" })
    void 요리중_식사중인_테이블_빈_테이블_변경(OrderStatus orderStatus) {
        OrderTable 주문_테이블 = new OrderTable(5, false);
        Order order = new Order(주문_테이블, orderStatus);

        assertThatThrownBy(() -> 주문_테이블.changeEmpty(true, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 손님 수를 음수로 변경할 수 없다.")
    @ParameterizedTest
    @ValueSource(ints = { -1, -2, -5, -10 })
    void 음수로_테이블_손님수_변경(int numberOfGuests) {
        assertThatThrownBy(() -> 단체_주문_테이블1.changeNumberOfGuests(numberOfGuests))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블은 손님 수를 변경할 수 없다.")
    @Test
    void 빈_테이블_손님수_변경() {
        OrderTable 주문_테이블 = new OrderTable(0, true);

        assertThatThrownBy(() -> 주문_테이블.changeNumberOfGuests(5)).isInstanceOf(IllegalArgumentException.class);
    }
}
