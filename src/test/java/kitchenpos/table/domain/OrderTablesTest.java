package kitchenpos.table.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class OrderTablesTest {
    private TableGroup 새로운_단체_테이블;
    private OrderTable 단체_주문_테이블1;
    private OrderTable 단체_주문_테이블2;
    private OrderTable 단체_주문_테이블3;
    private OrderTable 단체_주문_테이블4;
    private OrderTables 주문_테이블_목록;

    @BeforeEach
    void setUp() {
        TableGroup 단체_테이블 = new TableGroup();
        새로운_단체_테이블 = new TableGroup();

        단체_주문_테이블1 = new OrderTable(4, true);
        단체_주문_테이블2 = new OrderTable(3, true);
        단체_주문_테이블3 = new OrderTable(4, true);
        단체_주문_테이블4 = new OrderTable(3, true);

        ReflectionTestUtils.setField(단체_테이블, "id", 1L);
        ReflectionTestUtils.setField(새로운_단체_테이블, "id", 2L);
        ReflectionTestUtils.setField(단체_주문_테이블1, "id", 1L);
        ReflectionTestUtils.setField(단체_주문_테이블2, "id", 2L);
        ReflectionTestUtils.setField(단체_주문_테이블3, "id", 3L);
        ReflectionTestUtils.setField(단체_주문_테이블4, "id", 4L);

        주문_테이블_목록 = OrderTables.of(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2));
        주문_테이블_목록.group(단체_테이블.getId());
    }

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void 테이블_그룹_생성() {
        OrderTables 주문_테이블_목록 = OrderTables.of(Arrays.asList(단체_주문_테이블3, 단체_주문_테이블4));
        주문_테이블_목록.group(새로운_단체_테이블.getId());

        assertAll(
                () -> assertThat(주문_테이블_목록.getOrderTables()).hasSize(2),
                () -> assertTrue(주문_테이블_목록.getOrderTables().stream().noneMatch(OrderTable::isEmpty))
        );
    }

    @DisplayName("빈 주문 테이블 목록으로 테이블 그룹을 생성할 수 없다.")
    @Test
    void 빈_주문_테이블_목록_테이블_그룹_생성() {
        OrderTables 주문_테이블_목록 = new OrderTables();

        assertThatThrownBy(() -> 주문_테이블_목록.group(새로운_단체_테이블.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 목록의 크기가 2보다 작으면 테이블 그룹을 생성할 수 없다.")
    @Test
    void 주문_테이블_목록_2개미만_테이블_그룹_생성() {
        OrderTables 주문_테이블_목록 = OrderTables.of(Collections.singletonList(단체_주문_테이블3));

        assertThatThrownBy(() -> 주문_테이블_목록.group(새로운_단체_테이블.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("다른 테이블 그룹에 포함된 테이블로 테이블 그룹을 생성할 수 없다.")
    @Test
    void 다른_테이블_그룹에_포함된_테이블_테이블_그룹_생성() {
        OrderTables 주문_테이블_목록 = OrderTables.of(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블3));

        ReflectionTestUtils.setField(단체_주문_테이블1, "empty", true);
        ReflectionTestUtils.setField(단체_주문_테이블2, "empty", true);

        assertThatThrownBy(() -> 주문_테이블_목록.group(새로운_단체_테이블.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void 테이블_그룹_해제() {
        List<OrderTable> 그룹이였던_테이블_목록 = 주문_테이블_목록.getOrderTables();
        주문_테이블_목록.ungroup();

        assertThat(그룹이였던_테이블_목록.stream().noneMatch(OrderTable::hasTableGroup)).isTrue();
    }
}
