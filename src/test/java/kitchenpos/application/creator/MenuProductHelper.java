package kitchenpos.application.creator;

import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuProductRequest;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-11
 */
public class MenuProductHelper {

    public static MenuProduct create(long productId, int quantity) {
        return new MenuProduct(null, productId, quantity);
    }

    public static MenuProductRequest createRequest(long productId, int quantity) {
        return new MenuProductRequest(productId, quantity);
    }
}
