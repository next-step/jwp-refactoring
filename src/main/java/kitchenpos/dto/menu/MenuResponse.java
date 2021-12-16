package kitchenpos.dto.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import kitchenpos.domain.menu.Menu;
import kitchenpos.utils.StreamUtils;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    protected MenuResponse() {}

    private MenuResponse(Long id,
                         String name,
                         BigDecimal price,
                         Long menuGroupId,
                         List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(menu.getId(),
                                menu.getName().getValue(),
                                menu.getPrice().getValue(),
                                menu.getMenuGroupId(),
                                StreamUtils.mapToList(menu.getMenuProducts().getValues(), MenuProductResponse::from));
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

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MenuResponse that = (MenuResponse)o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
            && Objects.equals(price, that.price) && Objects.equals(menuGroupId, that.menuGroupId)
            && Objects.equals(menuProducts, that.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroupId, menuProducts);
    }
}
