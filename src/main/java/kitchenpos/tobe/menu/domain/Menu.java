package kitchenpos.tobe.menu.domain;

import kitchenpos.tobe.common.model.Price;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        menuProduct.registerMenu(this);
        this.menuProducts.add(menuProduct);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Menu menu = new Menu();

        public Builder id(Long id) {
            menu.id = id;
            return this;
        }

        public Builder name(String name) {
            menu.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            menu.price = Price.of(price);
            return this;
        }

        public Builder menuGroupId(Long menuGroupId) {
            menu.menuGroupId = menuGroupId;
            return this;
        }

        public Builder menuProducts(MenuProducts menuProducts) {
            menu.menuProducts = menuProducts;
            return this;
        }

        public Menu builder() {
            return menu;
        }
    }
}
