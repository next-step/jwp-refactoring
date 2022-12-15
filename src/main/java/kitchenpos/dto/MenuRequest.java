package kitchenpos.dto;

import kitchenpos.domain.Price;

import java.util.List;
import java.util.stream.Collectors;


public class MenuRequest {
    private String name;
    private Price price;
    private Long menuGroupId;

    private List<MenuProductRequest> menuProduct;

    public MenuRequest(String name, Price price, Long menuGroupId, List<MenuProductRequest> menuProduct) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProduct = menuProduct;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<Long> getProductId() {
        return menuProduct.stream().map(MenuProductRequest::getProductId).collect(Collectors.toList());
    }
}
