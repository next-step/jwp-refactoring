package kitchenpos.domain;

import kitchenpos.exception.InvalidPriceException;

import javax.persistence.*;
import java.math.BigDecimal;
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

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this(null, name, price, menuGroup);
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
        return menuProducts.getMenuProducts();
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        checkValidPrice(menuProducts);
        this.menuProducts.addAll(this, menuProducts);
    }

    private void checkValidPrice(List<MenuProduct> menuProducts) {
        BigDecimal sum = menuProducts.stream()
                .map(MenuProduct::getAmount)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        if (price.greaterThan(sum)) {
            throw new InvalidPriceException(price);
        }
    }

}
