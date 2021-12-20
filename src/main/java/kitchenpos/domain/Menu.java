package kitchenpos.domain;

import kitchenpos.exception.MismatchPriceException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        comparePrice(price, menuProducts);
        addMenuProducts(menuProducts);
        this.name = name;
        this.price = Price.of(price);
        this.menuGroup = menuGroup;
    }

    private void addMenuProducts(List<MenuProduct> newMenuProducts) {
        newMenuProducts.forEach(
            menuProduct -> menuProducts.add(menuProduct.by(this))
        );
    }

    private void comparePrice(BigDecimal price, List<MenuProduct> menuProducts) {
        if (price.compareTo(totalPrice(menuProducts)) != 0) {
            throw new MismatchPriceException();
        }
    }

    private BigDecimal totalPrice(List<MenuProduct> menuProducts) {
        return menuProducts.stream().map(MenuProduct::price)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
