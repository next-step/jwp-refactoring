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

import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;

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

        public Menu build() {
            return new Menu(name, price, menuGroupId, menuProducts);
        }
    }

    protected Menu() {}

    private Menu(Name name, Price price, MenuGroupId menuGroupId, MenuProducts menuProducts) {
        validateNonNull(name, price, menuGroupId, menuProducts);
        validatePriceCheaperThanMenuProducts(price, menuProducts);
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
        menuProducts.toMenu(this);
    }

    public static Menu create(Name name, Price price, MenuGroupId menuGroupId, MenuProducts menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
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

    private void validatePriceCheaperThanMenuProducts(Price price, MenuProducts menuProducts) {
        if (menuProducts.isMoreExpensiveThan(price)) {
            throw new IllegalArgumentException("메뉴의 가격이 메뉴와 연결된 상품의 수량 * 가격 보다 비쌀 수 없습니다.");
        }
    }

    private void validateNonNull(Name name, Price price, MenuGroupId menuGroupId, MenuProducts menuProducts) {
        if (isNull(name) || isNull(price) || isNull(menuGroupId) || isNull(menuProducts)) {
            throw new IllegalArgumentException("메뉴의 이름, 가격, 메뉴그룹, 메뉴상품리스트 는 필수정보입니다.");
        }
    }
}
