package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.menugroup.domain.MenuGroup;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    private Menu(Builder builder) {
        this(builder.id, builder.name, builder.price, builder.menuGroup, builder.menuProducts);
    }

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
        this(id, name, price, menuGroup, new MenuProducts());
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this(id, name, new Price(price), menuGroup, menuProducts);
    }

    public Menu(Long id, String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = new MenuProducts(menuProducts.value());
        validate();
    }

    private void validate() {
        validateNonNullFields();
        validateTotalPrice();
    }

    private void validateNonNullFields() {
        if (name == null || price == null || menuGroup == null) {
            throw new IllegalArgumentException("이름, 가격, 메뉴 그룹은 메뉴의 필수 사항입니다.");
        }
    }

    private void validateTotalPrice() {
        if (price.isGreaterThan(menuProducts.totalAmount())) {
            throw new IllegalArgumentException("메뉴의 가격은 메뉴 상품의 전체 금액보다 클 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal price() {
        return price.value();
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.value();
    }

    @Override
    public String toString() {
        return "Menu{" + "id=" + id + ", name='" + name + '\'' + ", price=" + price + ", menuGroup=" + menuGroup
                + ", menuProducts=" + menuProducts + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id) && Objects.equals(name, menu.name) && Objects.equals(price, menu.price)
                && Objects.equals(menuGroup, menu.menuGroup) && Objects.equals(menuProducts, menu.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroup, menuProducts);
    }

    public static class Builder {
        private final Long id;
        private final String name;
        private final Price price;
        private final MenuGroup menuGroup;
        private final MenuProducts menuProducts;

        public Builder() {
            this(null, null, null, null, new MenuProducts());
        }

        public Builder(Long id, String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.menuGroup = menuGroup;
            this.menuProducts = menuProducts;
        }

        public Builder id(Long id) {
            return new Builder(id, name, price, menuGroup, menuProducts);
        }

        public Builder name(String name) {
            return new Builder(id, name, price, menuGroup, menuProducts);
        }

        public Builder price(Price price) {
            return new Builder(id, name, price, menuGroup, menuProducts);
        }

        public Builder price(BigDecimal price) {
            return new Builder(id, name, new Price(price), menuGroup, menuProducts);
        }

        public Builder menuGroup(MenuGroup menuGroup) {
            return new Builder(id, name, price, menuGroup, menuProducts);
        }

        public Builder menuProducts(MenuProducts menuProducts) {
            return new Builder(id, name, price, menuGroup, menuProducts);
        }

        public Menu build() {
            return new Menu(this);
        }
    }
}
