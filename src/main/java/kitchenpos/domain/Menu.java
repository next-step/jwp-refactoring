package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static kitchenpos.constants.ErrorCodeType.MENU_PRICE_NOT_OVER_SUM_PRICE;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(Long id, String name, Price price, MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public Menu(String name, Price price, MenuGroup menuGroup) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }


    private static void validCheckMeuProductPrice(List<MenuProduct> menuProducts, Price price) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getProduct()
                    .getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        validCheckMenuPrice(sum, price);
    }

    private static void validCheckMenuPrice(BigDecimal sumPrice, Price requestPrice) {
        if (requestPrice.getPrice().compareTo(sumPrice) > 0) {
            throw new IllegalArgumentException(MENU_PRICE_NOT_OVER_SUM_PRICE.getMessage());
        }
    }

    public void addMenuProducts(MenuProducts menuProducts) {
        validCheckMeuProductPrice(menuProducts.getList(), price);

        this.menuProducts = menuProducts;

        menuProducts.getList()
                .forEach(menuProduct -> menuProduct.setMenu(this));
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
        return menuProducts.getList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id) && Objects.equals(name, menu.name) && Objects.equals(price, menu.price) && Objects.equals(menuGroup, menu.menuGroup) && Objects.equals(menuProducts, menu.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroup, menuProducts);
    }
}
