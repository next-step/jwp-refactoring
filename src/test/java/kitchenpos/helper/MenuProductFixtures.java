package kitchenpos.helper;

import static kitchenpos.helper.ProductFixtures.반반치킨_상품;
import static kitchenpos.helper.ProductFixtures.양념치킨_상품;
import static kitchenpos.helper.ProductFixtures.통구이_상품;
import static kitchenpos.helper.ProductFixtures.후라이드치킨_상품;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;

public class MenuProductFixtures {

    public static MenuProductRequest 통구이_메뉴상품_요청  = new MenuProductRequest(통구이_상품.getId(), 1);
    public static MenuProductRequest 반반치킨_메뉴상품_요청 = new MenuProductRequest(반반치킨_상품.getId(), 1);

    public static MenuProduct 후라이드치킨_메뉴상품 = new MenuProduct(1, 후라이드치킨_상품);
    public static MenuProduct 양념치킨_메뉴 = new MenuProduct(1, 양념치킨_상품);
    public static MenuProduct 통구이_메뉴상품  = new MenuProduct(1,통구이_상품);
    public static MenuProduct 반반치킨_메뉴상품 = new MenuProduct(1, 반반치킨_상품);
}
