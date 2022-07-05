package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    private Long menuGroupId;

    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu() {
    }

    public Menu(String name, long price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(name, BigDecimal.valueOf(price), menuGroup, menuProducts);
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validateMenuPrice(price);
        validateTotalPrice(price, menuProducts);
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroup.getId();
        this.menuProducts.addAll(toMenuProducts(menuProducts));
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

    public void setMenuGroupId(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    private void validateMenuPrice(BigDecimal price) {
        if (isNull(price) || isLessThanZero(price)) {
            throw new IllegalArgumentException("메뉴 가격은 0원 보다 작을 수 없습니다.");
        }
    }

    private boolean isNull(BigDecimal price) {
        return Objects.isNull(price);
    }

    private boolean isLessThanZero(BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) < 0;
    }

    private void validateTotalPrice(BigDecimal price, List<MenuProduct> menuProducts) {
        AtomicReference<BigDecimal> sum = new AtomicReference<>(BigDecimal.ZERO);
        menuProducts.forEach(
                menuProduct -> sum.set(sum.get().add(menuProduct.getProductPrice()
                                                                .multiply(BigDecimal.valueOf(menuProduct.getQuantity())))));
        if (sum.get().compareTo(price) != 0) {
            throw new IllegalArgumentException("메뉴 가격과 상품 합계의 가격이 일치하지 않습니다");
        }
    }

    private List<MenuProduct> toMenuProducts(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                           .map(menuProduct -> new MenuProduct(this, menuProduct.getProduct(), menuProduct.getQuantity()))
                           .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id)
                && Objects.equals(name, menu.name)
                && Objects.equals(price, menu.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", menuProducts=" + menuProducts +
                '}';
    }
}
