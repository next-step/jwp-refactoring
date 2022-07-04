package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.product.dto.MenuProductResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    public static MenuResponse of(Menu menu) {
        MenuProducts menuProducts = menu.getMenuProducts();

        List<MenuProductResponse> menuProductResponses = menuProducts.getMenuProducts().stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());

        return MenuResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice().getPrice())
                .menuGroupId(menu.getMenuGroup().getId())
                .menuProducts(menuProductResponses)
                .build();
    }

}
