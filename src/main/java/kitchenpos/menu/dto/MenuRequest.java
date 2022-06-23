package kitchenpos.menu.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class MenuRequest {
    private String name;
    private long price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    @JsonCreator
    public MenuRequest(@JsonProperty("name") String name, @JsonProperty("price") long price,
            @JsonProperty("menuGroupId") Long menuGroupId,
            @JsonProperty("menuProducts") List<MenuProductRequest> menuProducts) {
        check(name, price);
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public long getPrice() {
        return price;
    }

    public void check(long price) {
        if (this.price > price) {
            throw new IllegalArgumentException();
        }
    }

    private void check(String name, long price) {
        if (Objects.isNull(price) || price < 0) {
            throw new IllegalArgumentException();
        }

        if (Objects.isNull(name)) {
            throw new IllegalArgumentException();
        }
    }
}
