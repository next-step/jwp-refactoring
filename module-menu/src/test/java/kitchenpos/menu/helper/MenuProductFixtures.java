package kitchenpos.menu.helper;

import static kitchenpos.helper.ProductFixtures.반반치킨_상품;
import static kitchenpos.helper.ProductFixtures.양념치킨_상품;
import static kitchenpos.helper.ProductFixtures.통구이_상품;
import static kitchenpos.helper.ProductFixtures.후라이드치킨_상품;

import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;

public class MenuProductFixtures {
    public static MenuProductRequest 통구이_메뉴상품_요청  = new MenuProductRequest(통구이_상품.getId(), 1);
    public static MenuProductRequest 반반치킨_메뉴상품_요청 = new MenuProductRequest(반반치킨_상품.getId(), 1);

    public static MenuProduct 후라이드치킨_메뉴상품 = 메뉴_상품_만들기(1, 후라이드치킨_상품);
    public static MenuProduct 양념치킨_메뉴상품 = 메뉴_상품_만들기(1, 양념치킨_상품);
    public static MenuProduct 통구이_메뉴상품  = 메뉴_상품_만들기(1,통구이_상품);
    public static MenuProduct 반반치킨_메뉴상품 = 메뉴_상품_만들기(1, 반반치킨_상품);

    public static MenuProduct 메뉴_상품_만들기(Integer quantity, Product product){
        return new MenuProduct(new Quantity(quantity), product.getId());
    }
}
