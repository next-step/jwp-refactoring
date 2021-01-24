package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.dto.MenuGroupResponse;

import java.math.BigDecimal;
import java.util.List;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroupResponse;
    private List<MenuProductResponse> menuProductResponses;

    public MenuResponse() {
    }

    public MenuResponse(Long id, String name, BigDecimal price, MenuGroupResponse menuGroupResponse,
                        List<MenuProductResponse> menuProductResponses) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupResponse = menuGroupResponse;
        this.menuProductResponses = menuProductResponses;
    }

    public static MenuResponse of(Menu menu) {
        return new MenuResponse(menu.getId()
                , menu.getName()
                , menu.getPrice().getValue()
                , MenuGroupResponse.of(menu.getMenuGroup())
                , MenuProductResponse.ofList(menu.getMenuProducts()));
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

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses;
    }
}
