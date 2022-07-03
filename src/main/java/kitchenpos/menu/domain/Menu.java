package kitchenpos.menu.domain;
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
    @JoinColumn(name = "menu_group_id", nullable = false)
    private Long menuGroupId;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public static final String ERROR_PRICE_TOO_HIGH = "가격은 %d 초과일 수 없습니다.";

    protected Menu() {
    }

    private Menu(Long id, String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = MenuPrice.from(price);
        this.menuGroupId = menuGroupId;
        registerMenuProducts(menuProducts);
    }

    private Menu(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = MenuPrice.from(price);
        this.menuGroupId = menuGroupId;
        registerMenuProducts(menuProducts);
    }

    public static Menu of(Long id, String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroupId, menuProducts);
    }

    public static Menu of(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    private void registerMenuProducts(List<MenuProduct> menuProducts) {
        validateMenuPrice(menuProducts);
        this.menuProducts = menuProducts;
        menuProducts.forEach(menuProduct -> menuProduct.registerMenu(this));
    }

    private void validateMenuPrice(List<MenuProduct> menuProducts) {
        int sumOfProductPrice = menuProducts.stream().
                mapToInt(menuProduct -> menuProduct.getProduct().getPrice() * menuProduct.getQuantity()).
                sum();
        if (price.isLargerThan(sumOfProductPrice)) {
            throw new IllegalPriceException(String.format(ERROR_PRICE_TOO_HIGH, sumOfProductPrice));
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
