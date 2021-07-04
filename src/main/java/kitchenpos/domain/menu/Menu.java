package kitchenpos.domain.menu;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    @OneToOne(fetch = LAZY, orphanRemoval = true)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @OneToMany(fetch = LAZY, mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    // for jpa
    public Menu() {
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(null, name, price, menuGroup, menuProducts);
    }

    private void setMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    private Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        setPrice(price);
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;

    }

    private void setPrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Should expect price is over zero");
        }
        this.price = price;
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

    public void changePrice(BigDecimal price) {
        setPrice(price);
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }

    public boolean isReasonablePrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : this.menuProducts) {
            final Product product = menuProduct.getProduct();
            sum = sum.add(product.multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return this.price.compareTo(sum) <= 0;
    }

    public void addMenuProducts(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
        menuProduct.setMenu(this);
    }

    public void clearMenuProducts() {
        this.menuProducts.clear();
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

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", menuGroup=" + menuGroup +
                ", menuProducts=" + menuProducts +
                '}';
    }
}
