package kitchenpos.domain.menu;

import kitchenpos.domain.menuGroup.MenuGroup;
import kitchenpos.domain.menuProduct.MenuProduct;
import kitchenpos.domain.menuProduct.MenuProducts;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MenuName name;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    public Menu() {

    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(id, name, price, menuGroup, new MenuProducts(menuProducts));
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.id = id;
        this.name = new MenuName(name);
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public void validateForCreate() {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal sum = menuProducts.calculateProductsSum();

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public void bindMenuProducts() {
        menuProducts.updateMenu(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}
