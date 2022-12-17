package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;

public class MenuResponse {
    private Long id;
    private Name name;
    private Price price;
    private Long menuGroupId;
    private List<ProductQuantityPair> menuProducts;

    public MenuResponse() {
    }

    public MenuResponse(Long id, Name name, Price price, Long menuGroupId,
        List<ProductQuantityPair> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroup().getId(),
            menu.getProductQuantityPairs());
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<ProductQuantityPair> getMenuProducts() {
        return menuProducts;
    }

    @JsonGetter("name")
    public String name() {
        return name.value();
    }

    @JsonGetter("price")
    public BigDecimal price() {
        return price.value();
    }

    @JsonSetter("price")
    public void setPrice(BigDecimal price) {
        this.price = new Price(price);
    }
}
