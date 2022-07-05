package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest(Long id, String name, BigDecimal price, Long menuGroupId,
                       List<MenuProductRequest> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    private static Long setMenuGroupIdFromMenu(Menu menu) {
        Long menuGroupId = null;
        if(menu.getMenuGroup()!= null){
            menuGroupId = menu.getMenuGroup().getId();
        }
        return menuGroupId;
    }

    private static List<MenuProductRequest> setMenuProductReqeustsFromMenu(Menu menu) {
        List<MenuProductRequest> menuProductRequests = null;
        if (menu.getMenuProducts() != null && menu.getMenuProductList()!= null) {
            menuProductRequests = menu.getMenuProducts().getMenuProducts().stream()
                    .map(MenuProductRequest::of)
                    .collect(Collectors.toList());
        }
        return menuProductRequests;
    }

    public static MenuRequest of(Menu menu){
        List<MenuProductRequest> menuProductRequests = setMenuProductReqeustsFromMenu(menu);
        Long menuGroupId = setMenuGroupIdFromMenu(menu);

        return new MenuRequest(menu.getId(),menu.getName(),menu.getPrice().getPrice(),menuGroupId, menuProductRequests );
    }
}
