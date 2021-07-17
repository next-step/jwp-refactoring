package kitchenpos.fixture;

import kitchenpos.domain.MenuProduct;

import java.util.List;

import static java.util.Arrays.asList;
import static kitchenpos.fixture.ProductFixture.*;

public class MenuProductFixture {
    public static MenuProduct 메뉴_상품_후라이드_치킨
            = new MenuProduct(1L, 1L, 상품_후라이드_치킨.getId(), 1L);
    public static MenuProduct 메뉴_상품_양념_치킨
            =  new MenuProduct(2L, 1L, 상품_양념_치킨.getId(), 1L);

    public static List<MenuProduct> 메뉴_상품_치킨_세트_리스트 = asList(
            메뉴_상품_후라이드_치킨,
            메뉴_상품_양념_치킨
    );
}
