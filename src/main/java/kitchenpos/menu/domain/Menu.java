package kitchenpos.menu.domain;

import kitchenpos.menu.exception.LimitPriceException;

import javax.persistence.*;
import java.math.BigDecimal;
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

    @JoinColumn(name = "menu_group_id")
    private Long menuGroupId;

    @Embedded
    private final MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = Price.of(price);
        this.menuGroupId = menuGroup.getId();
        addMenuProducts(menuProducts);
    }

    private void addMenuProducts(List<MenuProduct> newMenuProducts) {
        comparePrice(newMenuProducts);
        menuProducts.add(newMenuProducts);
    }

    private void comparePrice(List<MenuProduct> menuProducts) {
        if (price.value().compareTo(totalPrice(menuProducts)) > 0) {
            throw new LimitPriceException();
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.value();
    }
}
