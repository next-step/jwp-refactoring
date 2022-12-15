package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private BigDecimal price;
    @Column(name = "menu_group_id")
    private Long menuGroupId;
    @OneToMany(mappedBy = "menu",fetch = FetchType.LAZY)
    private List<MenuProduct> menuProducts;

    private Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static Menu of(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        validMinusPrice(price);
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    private static void validMinusPrice(BigDecimal price) {
        if (price.intValue() < 0) {
            throw new IllegalArgumentException("메뉴의 가격은 0 이상이어야 합니다");
        }
    }

    public Menu() {
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
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
        return Objects.equals(name, menu.name) && Objects.equals(price.intValue(), menu.price.intValue())
                && Objects.equals(menuGroupId, menu.menuGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, menuGroupId, menuProducts);
    }

    public List<Product> productList() {
        return this.menuProducts.stream().map(MenuProduct::getProduct)
                .collect(Collectors.toList());
    }

    public void checkValidPrice() {
        BigDecimal sum = totalPrice();
        if (this.price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 상품 가격의 총 합과 같거나 작아야합니다");
        }
    }

    private BigDecimal totalPrice() {
        return BigDecimal.valueOf(this.menuProducts.stream()
                .map(MenuProduct::totalProductPrice)
                .mapToInt(BigDecimal::intValue)
                .sum());

    }

    public void setMenuToMenuProducts() {
        this.menuProducts.forEach(it -> it.setMenu(this));
    }
}
