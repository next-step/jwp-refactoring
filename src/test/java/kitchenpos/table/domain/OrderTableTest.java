package kitchenpos.table.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class OrderTableTest {
    private TableGroup 단체_테이블;
    private OrderTable 단체_주문_테이블1;
    private OrderTables 주문_테이블_목록;

    @BeforeEach
    void setUp() {
        단체_테이블 = TableGroup.of(1L);
        단체_주문_테이블1 = new OrderTable(0, true);
        OrderTable 단체_주문_테이블2 = new OrderTable(0, true);
        주문_테이블_목록 = OrderTables.of(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2));
        주문_테이블_목록.group(단체_테이블.getId());
    }

    @DisplayName("테이블이 비어있지 않으면 테이블 그룹을 생성할 수 없다.")
    @Test
    void 비어있지_않은_테이블_테이블_그룹_생성() {
        OrderTable 비어있지_않은_테이블 = new OrderTable(5, false);
        OrderTables orderTables = OrderTables.of(Arrays.asList(단체_주문_테이블1, 비어있지_않은_테이블));

        assertThatThrownBy(() -> orderTables.group(단체_테이블.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void 테이블_그룹_해제() {
        주문_테이블_목록.ungroup();

        assertThat(단체_주문_테이블1.hasTableGroup()).isFalse();
    }

    @DisplayName("빈 테이블로 변경한다.")
    @Test
    void 빈_테이블_변경() {
        OrderTable 주문_테이블 = new OrderTable(5, false);

        주문_테이블.changeEmpty(true);

        assertTrue(주문_테이블.isEmpty());
    }

    @DisplayName("테이블 그룹에 포함된 테이블은 빈 테이블로 변경할 수 없다.")
    @Test
    void 테이블_그룹에_포함된_테이블_빈_테이블_변경() {
        assertThatThrownBy(() -> 단체_주문_테이블1.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
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
