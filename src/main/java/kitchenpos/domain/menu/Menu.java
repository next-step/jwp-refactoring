package kitchenpos.domain.menu;

import kitchenpos.domain.menuGroup.MenuGroup;
import kitchenpos.domain.menuProduct.MenuProduct;
import kitchenpos.domain.menuProduct.MenuProducts;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.MenuRequest;

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
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = new MenuProducts(menuProducts);
    }

    public static Menu of(MenuRequest menuRequest, MenuGroup menuGroup) {
        return new Menu(null, menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuRequest.getMenuProducts());
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

    public void setId(final Long id) {
        this.id = id;
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

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}
