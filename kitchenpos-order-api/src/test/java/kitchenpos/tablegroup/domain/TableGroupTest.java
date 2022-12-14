package kitchenpos.tablegroup.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
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

@DisplayName("단체 테이블을 관리하는 클래스 테스트")
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

    @Test
    void 주문_테이블_목록이_비어있으면_단체_테이블을_생성할_수_없음() {
        assertThatThrownBy(() -> {
            단체_테이블.group(Collections.emptyList());
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(TableGroupExceptionCode.ORDER_TABLES_CANNOT_BE_EMPTY.getMessage());
    }

    @Test
    void 주문_테이블_목록의_크기가_2개_보다_작으면_단체_테이블을_생성할_수_없음() {
        assertThatThrownBy(() -> {
            단체_테이블.group(Arrays.asList(단체_주문_테이블1));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(TableGroupExceptionCode.MUST_BE_GREATER_THAN_MINIMUM_SIZE.getMessage());
    }

    @Test
    void 다른_단체_테이블에_포함된_주문_테이블이_있으면_단체_테이블을_생성할_수_없음() {
        TableGroup 새로운_단체_테이블 = new TableGroup();
        단체_테이블.group(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2));

        단체_주문_테이블1.changeEmpty(true);
        단체_주문_테이블2.changeEmpty(true);

        assertThatThrownBy(() -> {
            새로운_단체_테이블.group(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableExceptionCode.ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP.getMessage());
    }

    @Test
    void 단체_테이블_생성() {
        단체_테이블.group(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2));

        assertAll(
                () -> assertThat(단체_테이블.getOrderTables()).hasSize(2),
                () -> assertTrue(단체_테이블.getOrderTables().stream().noneMatch(OrderTable::isEmpty))
        );
    }

    @Test
    void 단체_테이블_해제() {
        단체_테이블.group(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2));

        Order 주문1 = new Order(단체_주문_테이블1.getId(), OrderStatus.COMPLETION);
        Order 주문2 = new Order(단체_주문_테이블2.getId(), OrderStatus.COMPLETION);

        단체_테이블.ungroup();

        assertAll(
                () -> assertThat(단체_주문_테이블1.getTableGroup()).isNull(),
                () -> assertThat(단체_주문_테이블2.getTableGroup()).isNull()
        );
    }
}