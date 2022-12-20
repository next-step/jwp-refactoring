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


    private static void validCheckMeuProductPrice(BigDecimal sumPrice, Price price) {
        if (price.getPrice().compareTo(sumPrice) > 0) {
            throw new IllegalArgumentException(MENU_PRICE_NOT_OVER_SUM_PRICE.getMessage());
        }
    }

    public void addMenuProducts(MenuProducts menuProducts) {
        validCheckMeuProductPrice(menuProducts.getSumPrice(), price);

        this.menuProducts = menuProducts;
        menuProducts.setMenu(this);
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
}
