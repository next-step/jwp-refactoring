package kitchenpos.menu.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    @OneToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {
    }

    public Menu(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this(name, price, menuGroup, new MenuProducts());
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void changeName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void changePrice(final BigDecimal price) {
        this.price = price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public void setMenuGroup(MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    public void changeMenuProducts(final MenuProducts menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void addMenuProducts(MenuProduct menuProduct) {
        this.menuProducts.addMenuProducts(menuProduct);
    }


    /**
     * 요청 받은 메뉴의 가격을 검증합니다.
     * @throws IllegalArgumentException
     */
    public void validatePrice() {
        BigDecimal price = this.getPrice();
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }
}
