package kitchenpos.domain;

import kitchenpos.menu.domain.Price;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderMenu;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderLineItemsTest {

    @Test
    void 주문_항목이_있는지_확인한다() {
        // when & then
        assertThatThrownBy(() ->
                new OrderLineItems(new ArrayList<>())
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목이 없습니다.");
    }

    @Test
    void 메뉴_아이디_목록을_조회한다() {
        // when
        List<Long> result = createOrderLineItems().getMenuIds();

        // then
        assertThat(result).containsExactly(1L, 2L);
    }

    public static OrderLineItems createOrderLineItems() {
        return new OrderLineItems(Arrays.asList(
                new OrderLineItem(createOrderMenu(), 1),
                new OrderLineItem(createOrderMenu2(), 2)
        ));
    }

    public static OrderLineItems createDuplicateOrderLineItems() {
        return new OrderLineItems(Arrays.asList(
                new OrderLineItem(createOrderMenu(), 2),
                new OrderLineItem(createOrderMenu(), 2)
        ));
    }

    public static OrderMenu createOrderMenu() {
        return new OrderMenu(1L, "치킨", new Price(BigDecimal.valueOf(10000)));
    }

    public static OrderMenu createOrderMenu2() {
        return new OrderMenu(2L, "파닭", new Price(BigDecimal.valueOf(12000)));
    }
}
