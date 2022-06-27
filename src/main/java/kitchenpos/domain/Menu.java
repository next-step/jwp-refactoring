package kitchenpos.domain;

import kitchenpos.exception.InvalidPriceException;

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

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this.name = name;
        this.price = new Price(price);
        this.menuGroup = menuGroup;
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroup = menuGroup;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public BigDecimal getPrice() {
        return price.getValue();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        checkValidPrice(menuProducts);
        addAllMenuProducts(menuProducts);
    }

    private void checkValidPrice(List<MenuProduct> menuProducts) {
        BigDecimal sum = menuProducts.stream()
                .map(MenuProduct::getTotalPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        if (price.greaterThan(sum)) {
            throw new InvalidPriceException(price);
        }
    }

    private void addAllMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(this));
    }

}
