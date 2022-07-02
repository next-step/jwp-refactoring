package kitchenpos.menu.domain;

import kitchenpos.exception.ErrorMessage;
import kitchenpos.exception.IllegalPriceException;
import kitchenpos.menugroup.domain.MenuGroup;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private MenuPrice price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    private Menu(String name, int price, MenuGroup menuGroup) {
        this.name = name;
        this.price = MenuPrice.from(price);
        this.menuGroup = menuGroup;
    }

    public static Menu of(String name, int price, MenuGroup menuGroup){
        return new Menu(name, price, menuGroup);
    }

    public void registerMenuProducts(List<MenuProduct> menuProducts) {
        validateMenuPrice(menuProducts);
        this.menuProducts = menuProducts;
        menuProducts.forEach(menuProduct -> menuProduct.registerMenu(this));
    }

    private void validateMenuPrice(List<MenuProduct> menuProducts) {
        int sumOfProductPrice = menuProducts.stream().
                mapToInt(menuProduct -> menuProduct.getProduct().getPrice() * menuProduct.getQuantity()).
                sum();
        if (price.isLargerThan(sumOfProductPrice)) {
            throw new IllegalPriceException(String.format(ErrorMessage.ERROR_PRICE_TOO_HIGH, sumOfProductPrice));
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price.getValue();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
