package kitchenpos.menu.domain;

import kitchenpos.exception.MenuException;
import kitchenpos.product.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Menu {

    public static final String ILLEGAL_MENU_PRICE_ERROR_MESSAGE = "메뉴의 가격이 잘못 입력되었습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price = new Price();

    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
    }

    public Menu(Long id, String name, Price price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
    }

    public void addMenuProducts(MenuProduct menuProduct) {
        if (!menuProducts.contains(menuProduct)) {
            menuProduct.withMenu(this);
            menuProducts.addMenuProduct(menuProduct);
        }
    }


    public void validMenuTotalAmount(BigDecimal menuTotalAmount) {
        if (price.isGreaterThen(menuTotalAmount)) {
            throw new MenuException(ILLEGAL_MENU_PRICE_ERROR_MESSAGE);
        }
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

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}
