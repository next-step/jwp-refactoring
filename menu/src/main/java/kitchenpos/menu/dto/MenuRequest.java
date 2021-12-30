package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.product.domain.Product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class MenuRequest {

    @NotBlank
    private String name;

    @Positive
    @NotNull
    private BigDecimal price;

    @Positive
    @NotNull
    private Long menuGroupId;

    @Size(min = 1)
    private List<MenuProductRequest> products = new ArrayList<>();

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getProducts() {
        return products;
    }

    public Menu toEntity() {
        Menu menu = Menu.prepared(name, price);
        return menu;
    }

    public List<Long> getProductIds() {
        return this.products.stream()
                .map(MenuProductRequest::getProductId)
                .collect(toList());
    }

    public MenuProductRequest find(Product product) {
        return this.products.stream()
                .filter(p -> p.match(product))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }
}
