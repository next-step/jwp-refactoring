package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.dto.MenuGroupRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {
    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupRequest menuGroupRequest;
    private List<MenuProductRequest> menuProductRequests;

    public MenuRequest() {
    }

    public MenuRequest(Long id, String name, BigDecimal price, MenuGroupRequest menuGroupRequest,
                       List<MenuProductRequest> menuProductRequests) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupRequest = menuGroupRequest;
        this.menuProductRequests = menuProductRequests;
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

    public MenuGroupRequest getMenuGroupRequest() {
        return menuGroupRequest;
    }

    public List<MenuProductRequest> getMenuProductRequests() {
        return menuProductRequests;
    }

    public Menu toMenu() {
        return Menu.of(id, name, price, menuGroupRequest.toMenuGroup(), convertToMenuProducts());
    }

    private List<MenuProduct> convertToMenuProducts() {
        return menuProductRequests.stream()
                .map(MenuProductRequest::toMenuProduct)
                .collect(Collectors.toList());
    }
}
