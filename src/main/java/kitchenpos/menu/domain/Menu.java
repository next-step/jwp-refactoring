package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuProductDto;
import kitchenpos.menugroup.MenuGroup;
import kitchenpos.product.Product;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    @ManyToOne
    @JoinColumn(name = "menuGroupId")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        if (BigDecimal.ZERO.compareTo(price) > 0) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public Menu(String name, BigDecimal price, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuProducts = menuProducts;
    }

    public void addMenuProducts(List<MenuProduct> products) {
        this.menuProducts = products;
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

    public void checkValidation() {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getTotalPrice());
        }
        if (getPrice().compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }
}
