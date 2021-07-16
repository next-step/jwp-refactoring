package kitchenpos.menu.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private Price price;

    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"), nullable = false)
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts;

    public Menu() {
    }

    public Menu(Long id) {
        this.id = id;
    }

    public Menu(String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(0L, name, price, menuGroupId, menuProducts);
    }

    public Menu(Long id, String name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = new MenuProducts(menuProducts, this);
    }

    public int comparePriceTo(Price sum) {
        return this.price.compareTo(sum);
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

    public int getMenuProductsSize() {
        return menuProducts.size();
    }
}
