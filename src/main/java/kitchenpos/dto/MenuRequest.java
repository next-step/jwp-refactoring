package kitchenpos.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {
    @NotBlank
    private String name;
    @DecimalMin(value = "0.0")
    private BigDecimal price;
    @NotNull
    private Long menuGroupId;
    @Size(min = 1)
    private List<MenuProductRequest> menuProducts;

    @SuppressWarnings("unused")
    protected MenuRequest() {}

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
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

    public static MenuRequestBuilder builder() {
        return new MenuRequestBuilder();
    }

    public static final class MenuRequestBuilder {
        private String name;
        private BigDecimal price;
        private Long menuGroupId;
        private List<MenuProductRequest> menuProducts;

        private MenuRequestBuilder() {}

        public MenuRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MenuRequestBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public MenuRequestBuilder menuGroupId(Long menuGroupId) {
            this.menuGroupId = menuGroupId;
            return this;
        }

        public MenuRequestBuilder menuProducts(List<MenuProductRequest> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public MenuRequest build() {
            return new MenuRequest(name, price, menuGroupId, menuProducts);
        }
    }
}
