package kitchenpos.application.creator;

import kitchenpos.dto.MenuProductDto;
import kitchenpos.dto.ProductDto;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-11
 */
public class MenuProductHelper {

    public static MenuProductDto create(long productId, int quantity) {
        MenuProductDto menuProduct = new MenuProductDto();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public static MenuProductDto create(ProductDto product, int quantity) {
        MenuProductDto menuProduct = new MenuProductDto();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
