package kitchenpos.order.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderTablesTest {
    private TableGroup 단체_테이블;
    private TableGroup 새로운_단체_테이블;
    private OrderTable 단체_주문_테이블1;
    private OrderTable 단체_주문_테이블2;
    private OrderTable 단체_주문_테이블3;
    private OrderTable 단체_주문_테이블4;

    @BeforeEach
    void setUp() {
        단체_테이블 = new TableGroup();
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

        단체_테이블.group(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2));
    }

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void 테이블_그룹_생성() {
        OrderTables 주문_테이블_목록 = new OrderTables();
        주문_테이블_목록.group(새로운_단체_테이블, Arrays.asList(단체_주문_테이블3, 단체_주문_테이블4));

        assertAll(
                () -> assertThat(새로운_단체_테이블.getOrderTables().getOrderTables()).hasSize(2),
                () -> assertTrue(새로운_단체_테이블.getOrderTables().getOrderTables().stream().noneMatch(OrderTable::isEmpty))
        );
    }

    @DisplayName("빈 주문 테이블 목록으로 테이블 그룹을 생성할 수 없다.")
    @Test
    void 빈_주문_테이블_목록_테이블_그룹_생성() {
        OrderTables 주문_테이블_목록 = new OrderTables();

        assertThatThrownBy(() -> 주문_테이블_목록.group(새로운_단체_테이블, Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 목록의 크기가 2보다 작으면 테이블 그룹을 생성할 수 없다.")
    @Test
    void 주문_테이블_목록_2개미만_테이블_그룹_생성() {
        OrderTables 주문_테이블_목록 = new OrderTables();

        assertThatThrownBy(() -> 주문_테이블_목록.group(새로운_단체_테이블, Arrays.asList(단체_주문_테이블3)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("다른 테이블 그룹에 포함된 테이블로 테이블 그룹을 생성할 수 없다.")
    @Test
    void 다른_테이블_그룹에_포함된_테이블_테이블_그룹_생성() {
        OrderTables 주문_테이블_목록 = new OrderTables();

        ReflectionTestUtils.setField(단체_주문_테이블1, "empty", true);
        ReflectionTestUtils.setField(단체_주문_테이블2, "empty", true);

        assertThatThrownBy(() -> 주문_테이블_목록.group(새로운_단체_테이블, Arrays.asList(단체_주문_테이블1, 단체_주문_테이블3)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블을 추가한다.")
    @Test
    void 주문_테이블_추가() {
        OrderTables 주문_테이블_목록 = new OrderTables();
        주문_테이블_목록.addOrderTable(새로운_단체_테이블, 단체_주문_테이블3);
        주문_테이블_목록.addOrderTable(새로운_단체_테이블, 단체_주문_테이블4);

        assertThat(새로운_단체_테이블.getOrderTables().getOrderTables()).hasSize(2);
    }

    @DisplayName("이미 등록된 주문 테이블은 추가되지 않는다.")
    @Test
    void 이미_등록된_주문_테이블_추가() {
        OrderTables 주문_테이블_목록 = new OrderTables();
        주문_테이블_목록.addOrderTable(새로운_단체_테이블, 단체_주문_테이블3);
        주문_테이블_목록.addOrderTable(새로운_단체_테이블, 단체_주문_테이블4);
        주문_테이블_목록.addOrderTable(새로운_단체_테이블, 단체_주문_테이블4);

        assertThat(새로운_단체_테이블.getOrderTables().getOrderTables()).hasSize(2);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void 테이블_그룹_해제() {
        OrderTables 주문_테이블_목록 = new OrderTables();
        주문_테이블_목록.addOrderTable(새로운_단체_테이블, 단체_주문_테이블3);
        주문_테이블_목록.addOrderTable(새로운_단체_테이블, 단체_주문_테이블4);

        주문_테이블_목록.ungroup();

        assertAll(
                () -> assertThat(단체_주문_테이블3.getTableGroupId()).isNull(),
                () -> assertThat(단체_주문_테이블4.getTableGroupId()).isNull()
        );
    }
}
