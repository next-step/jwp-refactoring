package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProductResponses;

    private MenuResponse() {}

    private MenuResponse(Long id, Name name, Price price, MenuGroup menuGroup, List<MenuProductResponse> menuProductResponses) {
        this.id = id;
        this.name = name.value();
        this.price = price.value();
        this.menuGroupId = menuGroup.getId();
        this.menuProductResponses = menuProductResponses;
    }

    public static MenuResponse from(Menu menu) {
        List<MenuProductResponse> menuProductResponses = menu.getMenuProducts().unmodifiableMenuProducts().stream().map(MenuProductResponse::from)
                .collect(Collectors.toList());
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroup(), menuProductResponses);
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

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses;
    }
}
