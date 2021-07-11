package kitchenpos.menu.domain;

import static java.util.Objects.*;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

@Entity
@Table
public class Menu {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "name", nullable = false))
    private Name name;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price", nullable = false))
    private Price price;

    @Embedded
    private MenuGroupId menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public static class Builder {
        private Name name;
        private Price price;
        private MenuGroupId menuGroupId;
        private MenuProducts menuProducts;
        private List<Product> products;

        public Builder name(Name name) {
            this.name = name;
            return this;
        }

        public Builder price(Price price) {
            this.price = price;
            return this;
        }

        public Builder menuGroupId(MenuGroupId menuGroupId) {
            this.menuGroupId = menuGroupId;
            return this;
        }

        public Builder menuProducts(MenuProducts menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public Builder products(List<Product> products) {
            this.products = products;
            return this;
        }

        public Menu build() {
            return new Menu(name, price, menuGroupId, menuProducts, products);
        }
    }

    protected Menu() {}

    private Menu(Name name, Price price, MenuGroupId menuGroupId, MenuProducts menuProducts, List<Product> products) {
        validateNonNull(name, price, menuGroupId, menuProducts, products);
        menuProducts.validatePrice(price, products);
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
        menuProducts.toMenu(this);
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    public MenuGroupId getMenuGroupId() {
        return this.menuGroupId;
    }

    private void validateNonNull(Name name, Price price, MenuGroupId menuGroupId, MenuProducts menuProducts, List<Product> products) {
        if (isNull(name) || isNull(price) || isNull(menuGroupId) || isNull(menuProducts) || isNull(products) || products.isEmpty()) {
            throw new IllegalArgumentException("메뉴의 필수정보가 부족합니다.");
        }
    }
}
