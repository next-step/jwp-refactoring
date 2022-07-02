package kitchenpos.menu.domain;

import kitchenpos.generic.Price;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {
    public static final long DEFAULT_VERSION = 0L;

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

    @Version
    private Long version;

    @Embedded
    private final MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    private Menu(final String name, final BigDecimal price, final MenuGroup menuGroup, Long version) {
        validateMenuGroup(menuGroup);
        this.name = name;
        this.price = new Price(price);
        this.menuGroup = menuGroup;
        this.version = version;
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
            final List<MenuProduct> menuProducts,
            final Long version
    ) {
        Menu menu = new Menu(name, price, menuGroup, version);
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

    public Long getVersion() {
        return version;
    }
}
