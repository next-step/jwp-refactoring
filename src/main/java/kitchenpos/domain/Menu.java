package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.List;

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
    @JoinColumn(nullable = false)
    private MenuGroup menuGroup;

    @Embedded
    private final MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    private Menu(final String name, final BigDecimal price, final MenuGroup menuGroup) {
        validateMenuGroup(menuGroup);
        this.name = name;
        this.price = new Price(price);
        this.menuGroup = menuGroup;
    }

    private void validateMenuGroup(final MenuGroup menuGroup) {
        if (menuGroup == null) {
            throw new IllegalArgumentException("메뉴 그룹이 필요합니다.");
        }
    }

    public static Menu createMenu(
            final String name,
            final BigDecimal price,
            final MenuGroup menuGroup,
            final List<MenuProduct> menuProducts
    ) {
        Menu menu = new Menu(name, price, menuGroup);
        menu.addMenuProducts(menuProducts);

        return menu;
    }

    private void addMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(this, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getElements();
    }

    public boolean hasPriceGreaterThan(final BigDecimal totalPrice) {
        if (totalPrice == null) {
            throw new IllegalArgumentException("총 상품 금액은 null일 수 없습니다.");
        }

        return price.isGreaterThan(totalPrice);
    }
}
