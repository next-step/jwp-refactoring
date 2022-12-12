package kitchenpos.menu.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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
    @JoinColumn(name = "id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    public Menu(long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;

        this.price = Price.of(price);
        this.menuGroup = menuGroup;
        this.menuProducts = MenuProducts.of(this, menuProducts);

    }

    public static Menu of(long id, String name, BigDecimal price) {
        return new Menu(id, name, price, null, null);
    }

    public static Menu of(long id, String name, BigDecimal price, MenuGroup menuGroup) {
        return new Menu(id, name, price, menuGroup, null);
    }

    public static Menu of(long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroup, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts.setMenuProducts(menuProducts);
    }

    public void checkPriceValid() {
        if (!this.price.isLessOrEqualTo(this.menuProducts.getSum())) {
            throw new IllegalArgumentException();
        }
    }
}
