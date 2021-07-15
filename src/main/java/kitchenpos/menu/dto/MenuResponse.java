package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;

public class MenuResponse {
    private long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroupResponse;
    private List<MenuProductReponse> menuProductResponses;

    public MenuResponse() {
    }

    public MenuResponse(long id, String name, BigDecimal price, MenuGroupResponse menuGroupResponse,
                        List<MenuProductReponse> menuProductResponses) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupResponse = menuGroupResponse;
        this.menuProductResponses = menuProductResponses;
    }

    public static MenuResponse of(Menu menu) {

        List<MenuProductReponse> menuProductResponses = menu.getMenuProducts().stream()
                .map(MenuProductReponse::of)
                .collect(Collectors.toList());

        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice().getMoney(),
                MenuGroupResponse.of(menu.getMenuGroup()), menuProductResponses);
    }

    public long getId() {
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

    public List<MenuProductReponse> getMenuProductResponses() {
        return menuProductResponses;
    }
}
