package kitchenpos.table.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class OrderTableTest {
    private TableGroup 단체_테이블;
    private OrderTable 단체_주문_테이블1;
    private OrderTable 단체_주문_테이블2;

    @BeforeEach
    void setUp() {
        단체_테이블 = new TableGroup();
        단체_주문_테이블1 = new OrderTable(0, true);
        단체_주문_테이블2 = new OrderTable(0, true);

        단체_테이블.group();
    }

    @DisplayName("빈 주문 테이블로 테이블 그룹을 생성할 수 없다.")
    @Test
    void 빈_주문_테이블_테이블_그룹_생성() {
        단체_주문_테이블1.ungroup();

        assertThatThrownBy(() -> 단체_주문_테이블1.checkOrderTableIsEmpty())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void 테이블_그룹_해제() {
        단체_주문_테이블1.ungroup();

        assertThat(단체_주문_테이블1.getTableGroupId()).isNull();
    }

    @DisplayName("빈 테이블로 변경한다.")
    @Test
    void 빈_테이블_변경() {
        OrderTable 주문_테이블 = new OrderTable(5, false);

        assertTrue(주문_테이블.isEmpty());
    }

    @DisplayName("테이블 그룹에 포함된 테이블은 빈 테이블로 변경할 수 없다.")
    @Test
    void 테이블_그룹에_포함된_테이블_빈_테이블_변경() {
        ReflectionTestUtils.setField(단체_주문_테이블1, "tableGroupId", 1L);
    }

    @DisplayName("요리중, 식사중인 테이블은 빈 테이블로 변경할 수 없다.")
    @Test
    void 요리중_식사중인_테이블_빈_테이블_변경() {
        OrderTable 주문_테이블 = new OrderTable(5, false);

    }

    @DisplayName("테이블 손님 수를 변경한다.")
    @Test
    void 테이블_손님_수_변경() {
        단체_주문_테이블1.changeNumberOfGuests(5);

        assertEquals(5, 단체_주문_테이블1.getNumberOfGuests());
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
