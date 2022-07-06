package kitchenpos.menu.domain;

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
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.dto.MenuResponse;

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
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public void addProduct(Long productId, Quantity quantity) {
        final MenuProduct menuProduct = new MenuProduct.Builder(this)
                .setProductId(productId)
                .setQuantity(quantity)
                .build();
        this.menuProducts.add(menuProduct);
    }

    public Long getId() {
        return id;
    }

    public MenuProducts products() {
        return menuProducts;
    }

    public boolean isMoreThan(Price price) {
        return this.price.isMoreThan(price);
    }

    public MenuResponse toMenuResponse() {
        return new MenuResponse(this.id, this.name, this.price, this.menuGroup.toMenuGroupResponse(),
                this.menuProducts.get());
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
                && Objects.equals(price, menu.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }

    Menu(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.price = builder.price;
        this.menuGroup = builder.menuGroup;
        this.menuProducts = builder.menuProducts;
    }

    public static class Builder {
        private Long id;
        private String name;
        private Price price;
        private MenuGroup menuGroup;
        private MenuProducts menuProducts = new MenuProducts();

        public Builder(String name) {
            this.name = name;
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPrice(Price price) {
            this.price = price;
            return this;
        }

        public Builder setMenuGroup(MenuGroup menuGroup) {
            this.menuGroup = menuGroup;
            return this;
        }

        public Builder setMenuProducts(MenuProducts menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public Menu build() {
            return new Menu(this);
        }
    }
}
