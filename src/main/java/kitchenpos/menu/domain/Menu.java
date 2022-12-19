package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "menu")
public class Menu {
    private static final String EXCEPTION_MESSAGE_MENU_PRICE_INVALID = "메뉴의 가격은 상품의 가격 총합보다 작거나 같아야 합니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "bigint(20)")
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "menu_group_id", nullable = false, columnDefinition = "bigint(20)", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;
    @Embedded
    private final MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this.name = new Name(name);
        this.price = new Price(price);
        this.menuGroup = menuGroup;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.value();
    }

    public BigDecimal getPrice() {
        return price.value();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.values();
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        for (MenuProduct menuProduct : menuProducts) {
            validateMenuPrice(this.price, menuProduct);
            this.addMenuProduct(menuProduct);
        }
    }

    private void addMenuProduct(final MenuProduct menuProduct) {
        menuProduct.addedBy(this.id);
        menuProducts.add(menuProduct);
    }

    private void validateMenuPrice(Price menuPrice, MenuProduct menuProduct) {
        BigDecimal productPrice = menuProduct.getProduct().getPrice();
        Long productQuantity = menuProduct.getQuantity();

        if (menuPrice.isLarger(productPrice.multiply(BigDecimal.valueOf(productQuantity)))) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_MENU_PRICE_INVALID);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu)) return false;
        Menu menu = (Menu) o;
        return Objects.equals(getName(), menu.getName()) && Objects.equals(getPrice(), menu.getPrice()) && Objects.equals(getMenuGroup(), menu.getMenuGroup());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPrice(), getMenuGroup());
    }
}
