package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.advice.exception.MenuException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this.name = name;
        this.price = new Price(price);
        this.menuGroup = menuGroup;
        this.menuProducts = new ArrayList<>();
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup,
        List<MenuProduct> menuProducts) {
        this(name, price, menuGroup);
        this.menuProducts = new ArrayList<>();
        updateMenuProducts(menuProducts);
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        menuProduct.updateMenuId(this.id);
        this.menuProducts.add(menuProduct);
    }

    public void updateMenuProducts(List<MenuProduct> menuProducts) {
        validatePriceSum(menuProducts);
        menuProducts.stream()
            .forEach(menuProduct -> menuProduct.updateMenuId(this.id));
        this.menuProducts.addAll(menuProducts);
    }

    public void validatePriceSum(List<MenuProduct> menuProducts) {
        BigDecimal sum = getSumOfPrices(menuProducts);

        if (price.compareTo(new Price(sum)) > 0) {
            throw new MenuException("메뉴 가격과 각 가격의 총합보다 큽니다", price.getMoney().longValue(),
                sum.longValue());
        }
    }

    private BigDecimal getSumOfPrices(List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = menuProduct.getProduct();
            sum = sum.add(product.priceMultiply(menuProduct.getQuantity()));
        }
        return sum;
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
        return menuProducts;
    }

    @Override
    public String toString() {
        return "Menu{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", price=" + price +
            ", menuGroupId=" + menuGroup.getId() +
            ", menuProductsSize=" + menuProducts.size() +
            '}';
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

        return name != null ? name.equals(menu.name) : menu.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
