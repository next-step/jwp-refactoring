package kitchenpos.menu.dto;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private long menuGroupId;
    private List<MenuProductResponse> menuProducts = new ArrayList<> ();

    public MenuResponse(Long id, String name, BigDecimal price, long menuGroupId, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(Menu menu) {
        List<MenuProductResponse> menuProducts = MenuProductResponse.listOf(menu.getMenuProducts());
        Name name = menu.getName();
        Price price = menu.getPrice();
        return new MenuResponse(menu.getId(), name.getName(), price.getPrice(), menu.getMenuGroupId(), menuProducts);
    }

    public static List<MenuResponse> listOf(List<Menu> menus) {
        return menus.stream()
                .map(it -> MenuResponse.of(it))
                .collect(Collectors.toList());
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

    public long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
