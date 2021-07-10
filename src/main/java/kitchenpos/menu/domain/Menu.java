package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.ui.exception.IllegalMenuPriceException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;

    public Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(Long id, Menu menu) {
        this(menu.name, menu.price, menu.menuGroupId);
        this.id = id;

    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public static Menu of(MenuRequest menuRequest) {
        Menu menu = new Menu(
                menuRequest.getName(),
                menuRequest.getPrice(),
                menuRequest.getMenuGroupId()
        );
        menu.validatePrice();

        return menu;
    }

    public void validatePrice() {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalMenuPriceException("메뉴의 가격이 없거나 음수입니다");
        }
    }

    public void compareMenuPriceToProductsSum(BigDecimal sum) {
        if (price.compareTo(sum) > 0) {
            throw new IllegalMenuPriceException("메뉴의 가격이 상품의 가격보다 큽니다");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }
}
