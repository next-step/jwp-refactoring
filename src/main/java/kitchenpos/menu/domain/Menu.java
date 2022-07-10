package kitchenpos.menu.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Table(name = "menu")
@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    private Menu(MenuBuilder menuBuilder) {
        this.id = menuBuilder.id;
        this.name = menuBuilder.name;
        this.price = Price.from(menuBuilder.price);
        this.menuGroup = menuBuilder.menuGroup;
        addMenuProducts(menuBuilder.menuProducts);
    }

    private void addMenuProducts(final List<MenuProduct> menuProducts) {
        menuProducts.forEach(this::addMenuProduct);
        validatePrice();
    }

    private void addMenuProduct(final MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
        menuProduct.addedBy(this);
    }

    private void validatePrice() {
        if (price.getValue().compareTo(menuProducts.totalMenuProductPrice()) > 0) {
            throw new IllegalStateException("메뉴의 가격이 메뉴 상품 목록 가격의 합보다 클 수 없습니다.");
        }
    }

    // Builder
    public static class MenuBuilder {
        private Long id;
        private String name;
        private BigDecimal price;
        private MenuGroup menuGroup;
        private List<MenuProduct> menuProducts;

        public MenuBuilder(final String name, final BigDecimal price) {
            this.name = name;
            this.price = price;
        }

        public MenuBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public MenuBuilder menuGroup(final MenuGroup menuGroup) {
            this.menuGroup = menuGroup;
            return this;
        }

        public MenuBuilder menuPrducts(final List<MenuProduct> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public Menu build() {
            return new Menu(this);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id)
                && Objects.equals(name, menu.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
