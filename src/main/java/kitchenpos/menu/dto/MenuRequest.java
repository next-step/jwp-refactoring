package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {


    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProductRequests;

    private MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests = menuProductRequests;
    }

    public static MenuRequest of(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests){
        return new MenuRequest(name, price, menuGroupId, menuProductRequests);
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

    public List<MenuProductRequest> getMenuProductRequests() {
        return menuProductRequests;
    }
}
