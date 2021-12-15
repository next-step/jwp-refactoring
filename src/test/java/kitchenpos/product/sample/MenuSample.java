package kitchenpos.product.sample;

import static kitchenpos.product.sample.MenuGroupSample.두마리메뉴;
import static kitchenpos.product.sample.ProductSample.십원치킨;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Menu;
import kitchenpos.product.domain.MenuProduct;

public class MenuSample {

    public static Menu 이십원_후라이드치킨_두마리세트() {
        Menu menu = spy(Menu.of(
            Name.from("이십원_후라이드치킨_두마리세트"),
            Price.from(BigDecimal.valueOf(2)),
            두마리메뉴(),
            Collections.singletonList(MenuProduct.of(십원치킨(), Quantity.from(2L)))
        ));
        lenient().when(menu.id())
            .thenReturn(1L);
        return menu;
    }
}
