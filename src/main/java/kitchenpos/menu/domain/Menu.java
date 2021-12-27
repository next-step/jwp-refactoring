package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.MenuGroupRequiredException;
import kitchenpos.common.exception.MenuProductSumPriceException;
import kitchenpos.menugroup.domain.MenuGroup;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {
    }

    public Menu(String name, int price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(name, new BigDecimal(price), menuGroup, menuProducts);
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.name = new Name(name);
        this.price = new Price(price);
        this.menuGroup = menuGroup;
        addMenuProducts(menuProducts);
        validateMenu(menuGroup, menuProducts);
    }

    public Menu(Long id, String name, int price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = new Name(name);
        this.price = new Price(BigDecimal.valueOf(price));
        this.menuGroup = menuGroup;
        addMenuProducts(menuProducts);
        validateMenu(menuGroup, menuProducts);
    }

    private void addMenuProducts(List<MenuProduct> menuProducts) {
        menuProducts.forEach(menuProduct -> {
            this.menuProducts.addMenuProduct(menuProduct);
            menuProduct.decideMenu(this);
        });
    }

    private void validateMenu(MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        Optional.ofNullable(menuGroup)
                .orElseThrow(MenuGroupRequiredException::new);

        if (!price.isPossibleMenu(menuProducts)) {
            throw new MenuProductSumPriceException();
        }
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }
}
