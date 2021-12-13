package kitchenpos.order.application.sample;

import static kitchenpos.product.application.sample.ProductSample.후라이드치킨;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemSample {

    public static OrderLineItem 후라이드치킨세트_두개() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(1L);
        orderLineItem.quantity(2);

        MenuGroup menuGroup = MenuGroup.from(Name.from("두마리메뉴"));
        when(menuGroup.id()).thenReturn(1L);

        Menu menu = spy(Menu.of(
            Name.from("후라이드치킨세트"),
            Price.from(BigDecimal.TEN),
            menuGroup,
            Collections.singletonList(후라이드치킨두마리())
        ));
        when(menu.id()).thenReturn(1L);
        orderLineItem.setMenuId(menu.id());
        return orderLineItem;
    }

    public static MenuProduct 후라이드치킨두마리() {
        MenuProduct menuProduct = spy(MenuProduct.of(후라이드치킨(), Quantity.from(2L)));
        when(menuProduct.seq()).thenReturn(1L);
        return menuProduct;
    }
}
