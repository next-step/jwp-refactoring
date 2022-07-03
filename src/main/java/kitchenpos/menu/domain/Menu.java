package kitchenpos.menu.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price;
    private Long menuGroupId;

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    public Menu(Long id, String name, int price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = Price.of(price);
        this.menuGroupId = menuGroupId;
    }

    public Menu(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = Price.of(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
        registerMenuProduct(menuProducts);
    }

    public static Menu of(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }


    private void registerMenuProduct(List<MenuProduct> menuProducts) {
        validatePrice(menuProducts);
        this.menuProducts = menuProducts;
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(this));
    }

    private void validatePrice(List<MenuProduct> menuProducts) {
        long priceSum = menuProducts.stream().
            mapToLong(menuProduct -> menuProduct.getProduct().getPrice() * menuProduct.getQuantity()).
            sum();
        if (price.isLargerThan(priceSum)) {
            throw new IllegalArgumentException();
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

    public int getPrice() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void add(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id) && Objects.equals(name, menu.name) && Objects.equals(price, menu.price) && Objects.equals(menuGroupId, menu.menuGroupId)
            && Objects.equals(menuProducts, menu.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroupId, menuProducts);
    }
}
