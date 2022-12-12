package kitchenpos.domain;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderLineItemsTest {
    @DisplayName("주문 항목이 비어있으면 예외가 발생한다.")
    @Test
    void emptyException() {
        // when & then
        assertThatThrownBy(() -> new OrderLineItems(new ArrayList<>()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.ORDER_LINE_ITEMS_IS_EMPTY.getMessage());
    }
}
