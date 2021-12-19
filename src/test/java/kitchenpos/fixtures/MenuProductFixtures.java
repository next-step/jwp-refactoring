package kitchenpos.fixtures;

import kitchenpos.domain.MenuProduct;

/**
 * packageName : kitchenpos.fixtures
 * fileName : MenuProductFixtures
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
public class MenuProductFixtures {
    public static MenuProduct createMenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(menuId);
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
