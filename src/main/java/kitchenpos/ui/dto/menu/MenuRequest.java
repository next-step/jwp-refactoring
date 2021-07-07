package kitchenpos.ui.dto.menu;

import kitchenpos.domain.Menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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

    public static MenuRequest of(Menu menu) {
        return new MenuRequest(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(), MenuProductRequest.ofList(menu.getMenuProducts()));
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

    public Menu toMenu() {
        return Menu.of(id, name, price, menuGroupId, menuProductRequests.stream()
                .map(MenuProductRequest::toMenuProduct)
                .collect(Collectors.toList()));
    }
}
