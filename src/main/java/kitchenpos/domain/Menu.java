package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;
    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    private Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup,
                 List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static Menu of(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validateMenu(price, menuGroup, menuProducts);

        return new Menu(id, name, price, menuGroup, menuProducts);
    }

    private static void validateMenu(final BigDecimal price, final MenuGroup menuGroup, final List<MenuProduct> menuProducts) {
        validatePrice(price, menuProducts);
        validateMenuGroup(menuGroup);
    }

    private static void validateMenuGroup(final MenuGroup menuGroup) {
        if (Objects.isNull(menuGroup)) {
            throw new IllegalArgumentException();
        }
    }

    private static void validatePrice(final BigDecimal price, final List<MenuProduct> menuProducts) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getSumOfPrice());
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
