package kitchenpos.fixtures;

import kitchenpos.dto.MenuProductRequest;

/**
 * packageName : kitchenpos.fixtures
 * fileName : MenuProductFixtures
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
public class MenuProductFixtures {
    public static MenuProductRequest 메뉴상품_한개() {
        return MenuProductRequest.of(1L, 1L);
    }

    public static MenuProductRequest 메뉴상품_두개() {
        return MenuProductRequest.of(2L, 2L);
    }

    public static MenuProductRequest 메뉴상품_세개() {
        return MenuProductRequest.of(3L, 3L);
    }
}
