package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Price;
import kitchenpos.product.dto.ProductResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroupResponse;
    private List<ProductResponse> ProductResponses;

    public MenuResponse(Long id, String name, BigDecimal price, MenuGroupResponse menuGroupResponse,
                        List<ProductResponse> ProductResponses) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupResponse = menuGroupResponse;
        this.ProductResponses = ProductResponses;
    }

    public static MenuResponse of (Menu menu) {
        Price price = menu.getPrice();
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice().getValue(),
                MenuGroupResponse.of(menu.getMenuGroup()),
                menu.getMenuProducts()
                        .getValue()
                        .stream()
                        .map(MenuProduct::getProduct)
                        .map(ProductResponse::of)
                        .collect(Collectors.toList()));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public MenuGroupResponse getMenuGroupResponse() {
        return menuGroupResponse;
    }

    public List<ProductResponse> getMenuProductResponses() {
        return ProductResponses;
    }
}
