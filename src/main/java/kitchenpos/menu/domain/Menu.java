package kitchenpos.menu.domain;

import kitchenpos.exception.IllegalPriceException;
import kitchenpos.menuGroup.domain.MenuGroup;

import javax.persistence.*;
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
    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    public Menu(Long id, String name, int price, MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = MenuPrice.from(price);
        this.menuGroup = menuGroup;
    }

    public Menu(String name, int price, MenuGroup menuGroup) {
        this.name = name;
        this.price = MenuPrice.from(price);
        this.menuGroup = menuGroup;
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

    public int getPrice() {
        return price.getValue();
    }

    public void setPrice(final int price) {
        this.price = MenuPrice.from(price);
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public void setMenuGroup(final MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void registerMenuProducts(List<MenuProduct> menuProducts) {
        validateProductsPrice(menuProducts);
        this.menuProducts = menuProducts;

        menuProducts.forEach(menuProduct -> menuProduct.setMenu(this));
    }

    private void validateProductsPrice(List<MenuProduct> menuProducts) {
        int sumPrice = menuProducts.stream().
                mapToInt(menuProduct -> menuProduct.getProduct().getPrice() * menuProduct.getQuantity()).
                sum();
        if (price.isLargerThan(sumPrice)) {
            throw new IllegalPriceException("가격은 %d 미만일 수 없습니다.");
        }
    }
}
