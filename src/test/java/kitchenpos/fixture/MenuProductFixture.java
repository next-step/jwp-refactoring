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

    public static MenuProduct 메뉴_상품_양념_치킨_두마리
            = new MenuProduct(3L, 2L, 상품_양념_치킨.getId(), 2L);

    public static MenuProduct 메뉴_상품_후라이드_치킨_두마리
            = new MenuProduct(4L, 3L, 상품_후라이드_치킨.getId(), 2L);

    public static MenuProduct 메뉴_상품_감자튀김
            = new MenuProduct(5L, 4L, 상품_감자튀김.getId(), 1L);

    public static MenuProduct 메뉴_상품_치즈볼
            = new MenuProduct(6L, 5L, 상품_치즈볼.getId(), 1L);

    public static MenuProduct 메뉴_상품_옛날통닭
            = new MenuProduct(7L, 6L, 상품_옛날통닭.getId(), 1L);

    public static List<MenuProduct> 메뉴_상품_양념_후라이드_두마리_치킨_세트
            = asList(
            메뉴_상품_후라이드_치킨,
            메뉴_상품_양념_치킨
    );

    public static List<MenuProduct> 메뉴_상품_양념_두마리_치킨_세트
            = asList(메뉴_상품_양념_치킨_두마리);

    public static List<MenuProduct> 메뉴_상품_후라이드_두마리_치킨_세트
            = asList(메뉴_상품_후라이드_치킨_두마리);
}
