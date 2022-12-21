package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;


public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;

    private List<MenuProductRequest> menuProduct;

    protected MenuRequest() {
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProduct) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProduct = menuProduct;
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

    public List<MenuProductRequest> getMenuProduct() {
        return menuProduct;
    }

}
