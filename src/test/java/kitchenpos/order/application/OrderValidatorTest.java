package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderFixture.*;
import static kitchenpos.order.domain.OrderLineItemFixture.*;
import static kitchenpos.table.domain.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.order.application.OrderValidator;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;

@DisplayName("주문 유효성 검사")
@SpringBootTest
public class OrderValidatorTest {

    @Autowired
    private OrderValidator orderValidator;

    @DisplayName("주문 등록 유효성 검사 - 빈 주문 항목")
    @Test
    void create_order_line_items_is_empty() {
        // given
        Long orderTableId = 1L;
        Order order = savedOrder(1L, orderTableId, Collections.emptyList());
        long menuCount = 0;
        OrderTable orderTable = savedOrderTable(orderTableId, false);

        // when, then
        assertThatThrownBy(() -> orderValidator.validateSave(order, orderTable, menuCount))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 유효성 검사 - 등록 되어 있지 않은 주문 항목 메뉴")
    @Test
    void create_order_line_items_not_exists() {
        // given
        Long orderTableId = 1L;
        Order order = savedOrder(1L, orderTableId, Arrays.asList(
            generateOrderLineItem(1L, 1L),
            generateOrderLineItem(2L, 2L))
        );
        long menuCount = 1;
        OrderTable orderTable = savedOrderTable(orderTableId, false);

        // when, then
        assertThatThrownBy(() -> orderValidator.validateSave(order, orderTable, menuCount))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 유효성 검사 - 주문 테이블 빈 테이블")
    @Test
    void create_order_table_is_empty() {
        // given
        Long orderTableId = 1L;
        Order order = savedOrder(1L, orderTableId, Arrays.asList(
            generateOrderLineItem(1L, 1L),
            generateOrderLineItem(2L, 2L))
        );
        long menuCount = 2;
        OrderTable orderTable = savedOrderTable(orderTableId, true);

        // when, then
        assertThatThrownBy(() -> orderValidator.validateSave(order, orderTable, menuCount))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
