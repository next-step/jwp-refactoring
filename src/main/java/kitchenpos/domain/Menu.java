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
    private BigDecimal price;
    private Long menuGroupId;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId) {
        this(name, price, menuGroupId);
        this.id = id;
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(name, price, menuGroupId, menuProducts);
        this.id = id;
    }

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        verifyAvailable(price);
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(name, price, menuGroupId);
        menuProducts.forEach(this::addMenuProduct);
    }

    private void verifyAvailable(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
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

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
