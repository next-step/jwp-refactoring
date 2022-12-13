package kitchenpos.application;

import static kitchenpos.domain.OrderFixture.*;
import static kitchenpos.domain.OrderLineItemFixture.*;
import static kitchenpos.domain.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;

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
        Order order = orderParam(orderTableId, Collections.emptyList());
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
        Order order = orderParam(orderTableId, Arrays.asList(
            orderLineItemParam(1L, 1),
            orderLineItemParam(2L, 2))
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
        Order order = orderParam(1L, Arrays.asList(
            orderLineItemParam(1L, 1),
            orderLineItemParam(2L, 2))
        );
        long menuCount = 2;
        OrderTable orderTable = savedOrderTable(orderTableId, true);

        // when, then
        assertThatThrownBy(() -> orderValidator.validateSave(order, orderTable, menuCount))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
