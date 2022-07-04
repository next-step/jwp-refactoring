package kitchenpos.dto;

import kitchenpos.domain.Menu;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class MenuRequest {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    private static Long setMenuGroupIdFromMenu(kitchenpos.domain.Menu menu) {
        Long menuGroupId = null;
        if(menu.getMenuGroup()!= null){
            menuGroupId = menu.getMenuGroup().getId();
        }
        return menuGroupId;
    }

    private static List<MenuProductRequest> setMenuProductReqeustsFromMenu(kitchenpos.domain.Menu menu) {
        List<MenuProductRequest> menuProductRequests = null;
        if (menu.getMenuProducts() != null && menu.getMenuProducts().getMenuProducts()!= null) {
            menuProductRequests = menu.getMenuProducts().getMenuProducts().stream()
                    .map(MenuProductRequest::of)
                    .collect(Collectors.toList());
        }
        return menuProductRequests;
    }

    public static MenuRequest of(Menu menu){
        List<MenuProductRequest> menuProductRequests = setMenuProductReqeustsFromMenu(menu);
        Long menuGroupId = setMenuGroupIdFromMenu(menu);

        return MenuRequest.builder().id(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice().getPrice())
                .menuGroupId(menuGroupId)
                .menuProducts(menuProductRequests)
                .build();
    }
}
