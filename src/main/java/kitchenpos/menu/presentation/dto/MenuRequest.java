package kitchenpos.menu.presentation.dto;

import java.math.BigDecimal;
import java.util.List;

public class MenuRequest {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProductRequests;

    protected MenuRequest() {
    }

    private MenuRequest(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductRequests = menuProductRequests;
    }

    public static MenuRequest of(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest(null, name, price, menuGroupId, menuProductRequests);
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProductRequests() {
        return menuProductRequests;
    }
}
