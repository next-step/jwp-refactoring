package kitchenpos.tablegroup.domain;

import kitchenpos.tablegroup.exception.OrderTableExceptionCode;
import kitchenpos.tablegroup.exception.TableGroupExceptionCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("단체 테이블에 포함된 주문 테이블 목록을 관리하는 클래스 테스트")
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

    @Test
    void 주문_테이블_목록이_비어있으면_테이블_그룹을_생성할_수_없음() {
        OrderTables 주문_테이블_목록 = new OrderTables();

        assertThatThrownBy(() -> {
            주문_테이블_목록.group(새로운_단체_테이블, Collections.emptyList());
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(TableGroupExceptionCode.ORDER_TABLES_CANNOT_BE_EMPTY.getMessage());
    }

    @Test
    void 주문_테이블_목록의_크기가_2보다_작으면_테이블_그룹을_생성할_수_없음() {
        OrderTables 주문_테이블_목록 = new OrderTables();

        assertThatThrownBy(() -> {
            주문_테이블_목록.group(새로운_단체_테이블, Arrays.asList(단체_주문_테이블3));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(TableGroupExceptionCode.MUST_BE_GREATER_THAN_MINIMUM_SIZE.getMessage());
    }

    @Test
    void 다른_테이블_그룹에_포함되어_있으면_테이블_그룹을_생성할_수_없음() {
        OrderTables 주문_테이블_목록 = new OrderTables();

        단체_주문_테이블1.changeEmpty(true);
        단체_주문_테이블2.changeEmpty(true);

        assertThatThrownBy(() -> {
            주문_테이블_목록.group(새로운_단체_테이블, Arrays.asList(단체_주문_테이블1, 단체_주문_테이블3));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableExceptionCode.ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP.getMessage());
    }

    @Test
    void 테이블_그룹_생성() {
        OrderTables 주문_테이블_목록 = new OrderTables();
        주문_테이블_목록.group(새로운_단체_테이블, Arrays.asList(단체_주문_테이블3, 단체_주문_테이블4));

        assertAll(
                () -> assertThat(새로운_단체_테이블.getOrderTables()).hasSize(2),
                () -> assertTrue(새로운_단체_테이블.getOrderTables().stream().noneMatch(OrderTable::isEmpty))
        );
    }

    @Test
    void 주문_테이블_추가() {
        OrderTables 주문_테이블_목록 = new OrderTables();
        주문_테이블_목록.addOrderTable(새로운_단체_테이블, 단체_주문_테이블3);
        주문_테이블_목록.addOrderTable(새로운_단체_테이블, 단체_주문_테이블4);

        assertThat(새로운_단체_테이블.getOrderTables()).hasSize(2);
    }

    @Test
    void 이미_등록된_주문_테이블은_추가되지_않음() {
        OrderTables 주문_테이블_목록 = new OrderTables();
        주문_테이블_목록.addOrderTable(새로운_단체_테이블, 단체_주문_테이블3);
        주문_테이블_목록.addOrderTable(새로운_단체_테이블, 단체_주문_테이블4);
        주문_테이블_목록.addOrderTable(새로운_단체_테이블, 단체_주문_테이블4);

        assertThat(새로운_단체_테이블.getOrderTables()).hasSize(2);
    }

    @Test
    void 테이블_그룹_해제() {
        OrderTables 주문_테이블_목록 = new OrderTables();
        주문_테이블_목록.addOrderTable(새로운_단체_테이블, 단체_주문_테이블3);
        주문_테이블_목록.addOrderTable(새로운_단체_테이블, 단체_주문_테이블4);

        주문_테이블_목록.ungroup();

        assertAll(
                () -> assertThat(단체_주문_테이블3.getTableGroup()).isNull(),
                () -> assertThat(단체_주문_테이블4.getTableGroup()).isNull()
        );
    }
}