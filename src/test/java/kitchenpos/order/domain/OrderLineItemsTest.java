package kitchenpos.order.domain;

import kitchenpos.order.exception.OrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

public class OrderLineItemsTest {
//    @Test
//    @DisplayName("주문 상품 없을때 오류 ")
//    void createException() {
//        // then
//        assertThatThrownBy(() ->  OrderLineItems.of(Collections.emptyList()))
//                .isInstanceOf(OrderException.class);
//    }

//    @Test
//    @DisplayName("주문 상품 수량 안맞음")
//    void notMatchCount() {
//        // given
//        final OrderLineItems orderLineItems = OrderLineItems.of(Arrays.asList(OrderLineItem.of(1L, 5L)));
//        // then
//        assertThatThrownBy(() -> orderLineItems.validMenuSize(2))
//                .isInstanceOf(OrderException.class);
//    }
}
