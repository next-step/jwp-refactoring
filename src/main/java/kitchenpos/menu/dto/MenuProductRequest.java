package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

/**
 * packageName : kitchenpos.dto
 * fileName : MenuProductRequest
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public class MenuProductRequest {
    private Long productId;
    private Long quantity;

    private MenuProductRequest() {
    }

    private MenuProductRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductRequest of(Long productId, Long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public MenuProduct toEntity(Product product) {
        return new MenuProduct(product, quantity);
    }

    public MenuProduct toEntity() {
        return new MenuProduct(productId, quantity);
    }
}
