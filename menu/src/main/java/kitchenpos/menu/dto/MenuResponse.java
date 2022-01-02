package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroup menuGroup;
    private List<MenuProductResponse> menuProductResponses;

    public MenuResponse(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProductResponse> menuProductResponses) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProductResponses = menuProductResponses;
    }

    public static MenuResponse of(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.toBigDecimal(), menu.getMenuGroup(), createMenuProducts(menu.getMenuProducts()));
    }

    private static List<MenuProductResponse> createMenuProducts(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> MenuProductResponse.of(menuProduct))
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProductResponses;
    }

    public List<Long> createSeqList() {
        return menuProductResponses.stream()
                .map(menuProductResponse -> menuProductResponse.getSeq())
                .collect(Collectors.toList());
    }
}
