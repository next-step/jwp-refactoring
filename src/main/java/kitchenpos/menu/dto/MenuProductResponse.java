package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class MenuProductResponse {

    private long productId;

    private long quantity;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public MenuProductResponse() {
    }

    public MenuProductResponse(long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
    public static MenuProductResponse of(MenuProduct menuProduct) {
        Product product = menuProduct.getProduct();
        MenuProductResponse menuProductResponse = new MenuProductResponse();
        if(product != null) {
            menuProductResponse.setProductId(menuProductResponse.getProductId());
        }
        menuProductResponse.setQuantity(menuProduct.getQuantity());
        return menuProductResponse;
    }
}
