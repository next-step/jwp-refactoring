package kitchenpos.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

public class MenuCreateRequest {

    @NotBlank
    private String name;

    @DecimalMin(value = "0")
    private BigDecimal price;

    public MenuCreateRequest() {
    }

    public MenuCreateRequest(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public MenuCreateRequest(final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private Long menuGroupId;

    private List<MenuProduct> menuProducts;

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public static class MenuProduct {

        private Long productId;
        private Long quantity;

        public MenuProduct() {
        }

        public MenuProduct(final Long productId, final Long quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public Long getProductId() {
            return productId;
        }

        public Long getQuantity() {
            return quantity;
        }
    }
}
