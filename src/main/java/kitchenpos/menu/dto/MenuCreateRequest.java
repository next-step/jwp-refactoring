package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuCreateRequest {

    @NotBlank
    private String name;

    @DecimalMin(value = "0")
    private BigDecimal price;

    private Long menuGroupId;

    private List<MenuProductRequest> menuProducts;

    public MenuCreateRequest() {
    }

    public MenuCreateRequest(final String name, final BigDecimal price, final Long menuGroupId, final List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public static class MenuProductRequest {

        private Long productId;
        private Long quantity;

        public MenuProductRequest() {
        }

        public MenuProductRequest(final Long productId, final Long quantity) {
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

    public Menu toEntity() {
        final List<MenuProduct> menuProducts = this.menuProducts.stream()
                .map(menuProductRequest -> new MenuProduct(menuProductRequest.getProductId(), menuProductRequest.getQuantity()))
                .collect(Collectors.toList());

        return new Menu(getName(), getPrice(), getMenuGroupId(), menuProducts);
    }
}
