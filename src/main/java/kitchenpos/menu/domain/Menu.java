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
    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private MenuGroup menuGroup;
    private List<MenuProduct> menuProducts;

    public Menu(long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
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

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
