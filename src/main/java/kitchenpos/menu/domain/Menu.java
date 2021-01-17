package kitchenpos.menu.domain;

import kitchenpos.common.Money;
import kitchenpos.menugroup.domain.MenuGroup;

import javax.persistence.*;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Money price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(final String name, final Money price, final MenuGroup menuGroup, final List<MenuProduct> menuProducts) {
        this(name, price, menuGroup, new MenuProducts(menuProducts));
    }

    public Menu(final String name, final Money price, final MenuGroup menuGroup, final MenuProducts menuProducts) {
        validate(price, menuProducts);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    private void validate(final Money price, final MenuProducts menuProducts) {
        Money priceOfMenuProducts = menuProducts.price();
        if (price.isGreaterThan(priceOfMenuProducts)) {
            throw new IllegalArgumentException("메뉴 가격은 메뉴 상품들 가격의 합보다 높을 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }
}
