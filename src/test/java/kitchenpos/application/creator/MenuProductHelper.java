package kitchenpos.application.creator;

import kitchenpos.dto.MenuProductRequest;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-11
 */
public class MenuProductHelper {

    public static MenuProductRequest createRequest(long productId, int quantity) {
        return new MenuProductRequest(productId, quantity);
    }
}
