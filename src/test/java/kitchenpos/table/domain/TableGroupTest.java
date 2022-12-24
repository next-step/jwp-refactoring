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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TableGroupTest {
    private TableGroup 단체_테이블;
    private OrderTable 단체_주문_테이블1;
    private OrderTable 단체_주문_테이블2;

    @BeforeEach
    void setUp() {
        단체_테이블 = new TableGroup();
        단체_주문_테이블1 = new OrderTable(4, true);
        단체_주문_테이블2 = new OrderTable(3, true);

        ReflectionTestUtils.setField(단체_테이블, "id", 1L);
        ReflectionTestUtils.setField(단체_주문_테이블1, "id", 1L);
        ReflectionTestUtils.setField(단체_주문_테이블2, "id", 2L);
    }

    @DisplayName("단체 테이블을 지정한다.")
    @Test
    void 단체_지정() {
        단체_테이블.group(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2));

        assertAll(
                () -> assertThat(단체_테이블.getOrderTables().getOrderTables()).hasSize(2),
                () -> assertTrue(단체_테이블.getOrderTables().getOrderTables().stream().noneMatch(OrderTable::isEmpty))
        );
    }

    @DisplayName("빈 주문 테이블 목록으로 단체 지정을 할 수 없다.")
    @Test
    void 빈_주문_테이블목록_단체_지정() {
        assertThatThrownBy(() -> 단체_테이블.group(Collections.emptyList())).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("2개 미만 테이블 목록으로 단체 지정을 할 수 없다.")
    @Test
    void 테이블목록_2개미만_단체_지정() {
        assertThatThrownBy(() -> 단체_테이블.group(Collections.singletonList(단체_주문_테이블1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 단체 지정된 테이블은 단체 지정을 할 수 없다.")
    @Test
    void 기지정된_테이블_단체_지정() {
        TableGroup 새로운_단체_테이블 = new TableGroup();
        단체_테이블.group(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2));

        ReflectionTestUtils.setField(단체_주문_테이블1, "empty", true);
        ReflectionTestUtils.setField(단체_주문_테이블2, "empty", true);

        assertThatThrownBy(() -> 새로운_단체_테이블.group(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 테이블을 해제한다.")
    @Test
    void 단체_테이블_해제() {
        단체_테이블.group(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2));

        Order 주문1 = new Order(단체_주문_테이블1, OrderStatus.COMPLETION);
        Order 주문2 = new Order(단체_주문_테이블2, OrderStatus.COMPLETION);

        단체_테이블.ungroup(Arrays.asList(주문1, 주문2));

        assertAll(
                () -> assertThat(단체_주문_테이블1.getTableGroupId()).isNull(),
                () -> assertThat(단체_주문_테이블2.getTableGroupId()).isNull()
        );
    }

    @DisplayName("조리중, 식사중인 테이블은 단체 지정을 해제할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = { "COOKING", "MEAL" })
    void 조리중_식사중_테이블_단체_지정_해제(OrderStatus orderStatus) {
        단체_테이블.group(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2));

        Order 주문1 = new Order(단체_주문_테이블1, orderStatus);
        Order 주문2 = new Order(단체_주문_테이블2, OrderStatus.COMPLETION);

        assertThatThrownBy(() -> 단체_테이블.ungroup(Arrays.asList(주문1, 주문2)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
