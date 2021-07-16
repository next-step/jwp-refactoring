package kitchenpos.dto.request;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductRequest {
    private Long id;
    private Long productId;
    private Long quantity;

    public void setId(final Long id) {
        this.id = id;
    }

    public void setProductId(final Long productId) {
        this.productId = productId;
    }

    public void setQuantity(final Long quantity) {
        this.quantity = quantity;
    }

    public MenuProduct toEntity(Menu menu) {
        return new MenuProduct(id, menu, productId, quantity);
    }
}
