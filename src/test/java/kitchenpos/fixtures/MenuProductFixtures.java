package kitchenpos.fixtures;

import kitchenpos.domain.MenuProduct;

import java.util.Arrays;
import java.util.List;

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

    public static List<MenuProduct> createMenuProducts(MenuProduct... menuProducts) {
        return Arrays.asList(menuProducts);
    }
}