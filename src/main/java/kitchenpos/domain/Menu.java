package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @JoinColumn(name = "menu_group_id")
    @ManyToOne
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
        this(name, price, menuGroup);
        this.id = id;
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(name, price, menuGroup, menuProducts);
        this.id = id;
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this.name = name;
        this.price = new Price(price);
        this.menuGroup = menuGroup;
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(name, price, menuGroup);
        menuProducts.forEach(this::addMenuProduct);
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        if (!menuProducts.contains(menuProduct)) {
            menuProducts.add(menuProduct);
        }
        menuProduct.setMenu(this);
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
