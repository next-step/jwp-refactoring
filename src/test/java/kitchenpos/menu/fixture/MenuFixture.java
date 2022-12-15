package kitchenpos.menu.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Product;

public class MenuFixture {

    public static Menu 샘플_메뉴(){
        MenuGroup 중식 = MenuGroup.of("중식");
        Product 짜장면 = Product.of("짜장면", BigDecimal.valueOf(5000));
        Product 군만두 = Product.of("군만두", BigDecimal.valueOf(1000));
        MenuProduct 짜장면_메뉴 = MenuProduct.of(짜장면, 1);
        MenuProduct 군만두_메뉴 = MenuProduct.of(군만두, 2);
        MenuProducts 메뉴_상품_목록 = MenuProducts.of(Arrays.asList(짜장면_메뉴, 군만두_메뉴));
        return Menu.of("샘플_메뉴", BigDecimal.valueOf(6000), 중식, 메뉴_상품_목록);
    }

}
