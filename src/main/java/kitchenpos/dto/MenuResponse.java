package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroup;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse() {
    }

    public MenuResponse(Long id, String name, BigDecimal price, MenuGroupResponse menuGroup,
                        List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(Menu menu){
        List<MenuProductResponse> menuProductResponses = menu.getMenuProducts().stream()
                .map(
                        menuProduct -> new MenuProductResponse(menuProduct.getSeq(), menuProduct.getProduct().getName(),
                                menuProduct.getQuantity())
                ).collect(Collectors.toList());
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), MenuGroupResponse.of(menu.getMenuGroup()),
                menuProductResponses);
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

    public MenuGroupResponse getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
