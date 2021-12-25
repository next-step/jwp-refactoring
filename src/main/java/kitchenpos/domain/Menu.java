package kitchenpos.domain;

import kitchenpos.exception.IllegalMenuPriceException;
import kitchenpos.exception.NoMenuGroupException;

import javax.persistence.*;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    @Column(nullable = false)
    private Price price;
    @ManyToOne
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts;

    public Menu() {
    }

    public Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        validateMenuGroup();
        validateMenuPrice();
        menuProducts.changeMenu(this);
    }

    private void validateMenuGroup() {
        if (this.menuGroup == null) {    // TODO MenuProducts 내부로 이동
            throw new NoMenuGroupException();
        }
    }

    private void validateMenuPrice() {
        if (price.compareTo(menuProducts.getSumPrice()) > 0) {    // TODO MenuProducts 내부로 이동
            throw new IllegalMenuPriceException();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(final Price price) {
        this.price = price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public void setMenuGroup(MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final MenuProducts menuProducts) {
        this.menuProducts = menuProducts;
    }
}
