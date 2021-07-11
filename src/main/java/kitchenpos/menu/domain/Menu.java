package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    public Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        updateMenuProducts(menuProducts);
    }

    private void updateMenuProducts(MenuProducts menuProducts) {
        priceValidCheck(menuProducts.getTotalPrice());
        menuProducts.updateMenu(this);
        this.menuProducts = menuProducts;
    }

    private void priceValidCheck(BigDecimal totalPrice) {
        if (price.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException("메뉴 가격은 상품 가격의 총 합보다 클 수 없습니다.");
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProductsValues() {
        return menuProducts.values();
    }

    @Override
    public String toString() {
        return "MenuEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", menuGroup=" + getMenuGroupId() +
                ", menuProducts=" + getMenuProductsValues().size() +
                '}';
    }
}
