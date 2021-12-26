package kitchenpos.menu.domain;

import kitchenpos.product.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu() {
    }

    public Menu(String name, BigDecimal price) {
        this.name = name;
        this.price = new Price(price);
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = new Price(price);
        this.menuGroup = menuGroup;
        addMenuProducts(menuProducts);
    }

    private void addMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts.stream()
                .map(menuProduct -> new MenuProduct(this, menuProduct.getProduct(), menuProduct.getQuantity()))
                .collect(Collectors.toList()));
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
        return price.getPrice();
    }

    public void setPrice(final BigDecimal price) {
        this.price = new Price(price);
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public void setMenuGroup(final MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return this.menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
