package kitchenpos.menu.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;

public final class MenuRequest {

    private final String name;
    private final BigDecimal price;
    private final long menuGroupId;
    private final List<MenuProductRequest> menuProducts;

    @JsonCreator
    public MenuRequest(
        @JsonProperty("name") String name,
        @JsonProperty("price") BigDecimal price,
        @JsonProperty("menuGroupId") long menuGroupId,
        @JsonProperty("menuProducts") List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public Name name() {
        return Name.from(name);
    }

    public Price price() {
        return Price.from(price);
    }

    public Menu toEntity() {
        Menu menu = new Menu();
        menu.setPrice(price);
        menu.setName(name);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(
            menuProducts.stream()
                .map(MenuProductRequest::toEntity)
                .collect(Collectors.toList())
        );
        return menu;
    }
}
