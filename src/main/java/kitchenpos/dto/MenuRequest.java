package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import kitchenpos.domain.Name;
import kitchenpos.domain.Price;

public class MenuRequest {
    private Name name;
    private Price price;
    private Long menuGroupId;
    private List<ProductIdQuantityPair> menuProducts;

    public MenuRequest() {
    }

    public MenuRequest(Name name, Price price, Long menuGroupId,
        List<ProductIdQuantityPair> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public MenuRequest(List<ProductIdQuantityPair> menuProducts) {
        this.menuProducts = menuProducts;
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

    public List<ProductIdQuantityPair> getMenuProducts() {
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
