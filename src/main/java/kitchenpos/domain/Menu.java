package kitchenpos.domain;

import kitchenpos.exception.MenuPriceMoreThanMenuProductPriceSumException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu")
public class Menu extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 20)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", precision = 19, scale = 2, nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(final String name, final BigDecimal price, final MenuGroup menuGroup) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public Menu(final String name, final BigDecimal price, final MenuGroup menuGroup, final BigDecimal menuProductPriceSum) {
        this(name, price, menuGroup);
        checkMenuPrice(menuProductPriceSum);
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
        menuProduct.addMenu(this);
    }

    private void checkMenuPrice(BigDecimal menuProductPriceSum) {
        if (this.price.compareTo(menuProductPriceSum) > 0) {
            throw new MenuPriceMoreThanMenuProductPriceSumException(this.price.toPlainString());
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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
