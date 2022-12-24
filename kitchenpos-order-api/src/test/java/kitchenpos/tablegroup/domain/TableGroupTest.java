package kitchenpos.tablegroup.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.tablegroup.exception.TableExceptionConstants.ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP;
import static kitchenpos.tablegroup.exception.TableExceptionConstants.MUST_BE_GREATER_THAN_MINIMUM_SIZE;
import static kitchenpos.tablegroup.exception.TableExceptionConstants.ORDER_TABLES_CANNOT_BE_EMPTY;
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
                .hasMessage(ORDER_TABLES_CANNOT_BE_EMPTY.getErrorMessage());
    }

    @Test
    void 주문_테이블_목록의_크기가_2개_보다_작으면_단체_테이블을_생성할_수_없음() {
        assertThatThrownBy(() -> {
            단체_테이블.group(Arrays.asList(단체_주문_테이블1));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MUST_BE_GREATER_THAN_MINIMUM_SIZE.getErrorMessage());
    }

    @Test
    void 다른_단체_테이블에_포함된_주문_테이블이_있으면_단체_테이블을_생성할_수_없음() {
        TableGroup 새로운_단체_테이블 = new TableGroup();
        단체_테이블.group(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2));

        ReflectionTestUtils.setField(단체_주문_테이블1, "empty", new OrderTableEmpty(true));
        ReflectionTestUtils.setField(단체_주문_테이블2, "empty", new OrderTableEmpty(true));

        assertThatThrownBy(() -> {
            새로운_단체_테이블.group(Arrays.asList(단체_주문_테이블1, 단체_주문_테이블2));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP.getErrorMessage());
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
        MenuGroup menuGroup = new MenuGroup("양식");
        Menu menu1 = new Menu("양식 세트1", new BigDecimal(43000), menuGroup);
        Menu menu2 = new Menu("양식 세트2", new BigDecimal(50000), menuGroup);

        Order 주문1 = Order.from(단체_주문_테이블1.getId());
        Order 주문2 = Order.from(단체_주문_테이블2.getId());

        OrderLineItem orderLineItem1 = OrderLineItem.of(주문1, OrderMenu.of(menu1), 1L);
        OrderLineItem orderLineItem2 = OrderLineItem.of(주문2, OrderMenu.of(menu2), 1L);

        주문1.addOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
        주문2.addOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        주문1.changeOrderStatus(OrderStatus.COMPLETION);
        주문2.changeOrderStatus(OrderStatus.COMPLETION);

        단체_테이블.ungroup();

        assertAll(
                () -> assertThat(단체_주문_테이블1.getTableGroup()).isNull(),
                () -> assertThat(단체_주문_테이블2.getTableGroup()).isNull()
        );
    }
}
