package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;

/**
 * packageName : kitchenpos.dto
 * fileName : MenuProductRequest
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
//FIXME 생성자 제한하기
public class MenuProductRequest {
    private Long productId;
    private Long quantity;

    public MenuProductRequest() {
    }

    public MenuProductRequest(Long productId, Long quantity) {
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
