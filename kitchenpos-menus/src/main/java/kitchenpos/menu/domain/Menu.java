package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.Price;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @Column(nullable = false)
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    private Menu(Builder builder) {
        this(builder.id, builder.name, builder.price, builder.menuGroupId, builder.menuProducts);
    }

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, MenuProducts menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId) {
        this(id, name, price, menuGroupId, new MenuProducts());
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId, MenuProducts menuProducts) {
        this(id, name, Price.from(price), menuGroupId, menuProducts);
    }

    public Menu(Long id, String name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = new MenuProducts(menuProducts.value());
        validate();
    }

    private void validate() {
        validateNonNullFields();
    }

    private void validateNonNullFields() {
        if (name == null || price == null || menuGroupId == null) {
            throw new IllegalArgumentException("이름, 가격, 메뉴 그룹은 메뉴의 필수 사항입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", menuGroupId=" + menuGroupId +
                ", menuProducts=" + menuProducts +
                '}';
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
        return Objects.equals(id, menu.id) && Objects.equals(name, menu.name)
                && Objects.equals(price, menu.price) && Objects.equals(menuGroupId, menu.menuGroupId)
                && Objects.equals(menuProducts, menu.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroupId, menuProducts);
    }

    public static class Builder {
        private final Long id;
        private final String name;
        private final Price price;
        private final Long menuGroupId;
        private final MenuProducts menuProducts;

        public Builder() {
            this(null, null, null, null, new MenuProducts());
        }

        public Builder(Long id, String name, Price price, Long menuGroupId, MenuProducts menuProducts) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.menuGroupId = menuGroupId;
            this.menuProducts = menuProducts;
        }

        public Builder id(Long id) {
            return new Builder(id, name, price, menuGroupId, menuProducts);
        }

        public Builder name(String name) {
            return new Builder(id, name, price, menuGroupId, menuProducts);
        }

        public Builder price(Price price) {
            return new Builder(id, name, price, menuGroupId, menuProducts);
        }

        public Builder price(BigDecimal price) {
            return new Builder(id, name, Price.from(price), menuGroupId, menuProducts);
        }

        public Builder menuGroupId(Long menuGroupId) {
            return new Builder(id, name, price, menuGroupId, menuProducts);
        }

        public Builder menuProducts(MenuProducts menuProducts) {
            return new Builder(id, name, price, menuGroupId, menuProducts);
        }

        public Menu build() {
            return new Menu(this);
        }
    }
}
