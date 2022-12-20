package kitchenpos.tablegroup.domain;

import kitchenpos.common.ErrorCode;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderEmpty;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
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

public class TableGroupTest {

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
                .hasMessage(ErrorCode.ORDER_TABLES_CANNOT_BE_EMPTY.getErrorMessage());
    }

    @Test
    void 주문_테이블_목록의_크기가_2개_보다_작으면_단체_테이블을_생성할_수_없음() {
        assertThatThrownBy(() -> {
            단체_테이블.group(Arrays.asList(단체_주문_테이블1));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.MUST_BE_GREATER_THAN_MINIMUM_SIZE.getErrorMessage());
    }

    @Test
    void 다른_단체_테이블에_포함된_주문_테이블이_있으면_단체_테이블을_생성할_수_없음() {
        TableGroup 새로운_단체_테이블 = new TableGroup();
        단체_테이블.group(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2));

        ReflectionTestUtils.setField(단체_주문_테이블1, "empty", new OrderEmpty(true));
        ReflectionTestUtils.setField(단체_주문_테이블2, "empty", new OrderEmpty(true));

        assertThatThrownBy(() -> {
            새로운_단체_테이블.group(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP.getErrorMessage());
    }

    @Test
    void 단체_테이블_생성() {
        단체_테이블.group(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2));

        assertAll(
                () -> assertThat(단체_테이블.getOrderTables()).hasSize(2),
                () -> assertTrue(단체_테이블.getOrderTables().stream().noneMatch(OrderTable::isEmpty))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = { "COOKING", "MEAL" })
    void 조리중이거나_식사중인_주문_테이블이_있으면_단체_테이블을_해제할_수_없음(OrderStatus orderStatus) {
        단체_테이블.group(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2));

        Order 주문1 = new Order(단체_주문_테이블1, orderStatus);
        Order 주문2 = new Order(단체_주문_테이블2, OrderStatus.COMPLETION);

        assertThatThrownBy(() -> {
            단체_테이블.ungroup(Arrays.asList(주문1, 주문2));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.CANNOT_BE_CHANGED_ORDER_STATUS.getErrorMessage());
    }

    @Test
    void 단체_테이블_해제() {
        단체_테이블.group(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2));

        Order 주문1 = new Order(단체_주문_테이블1, OrderStatus.COMPLETION);
        Order 주문2 = new Order(단체_주문_테이블2, OrderStatus.COMPLETION);

        단체_테이블.ungroup(Arrays.asList(주문1, 주문2));

        assertAll(
                () -> assertThat(단체_주문_테이블1.getTableGroup()).isNull(),
                () -> assertThat(단체_주문_테이블2.getTableGroup()).isNull()
        );
    }
}
