package kitchenpos.domain;

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

    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    private Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this.name = name;
        this.price = new Price(price);
        this.menuGroup = menuGroup;
    }

    public static Menu createMenu(
            String name,
            BigDecimal price,
            MenuGroup menuGroup,
            List<MenuProduct> menuProducts
    ) {
        if (menuGroup == null) {
            throw new IllegalArgumentException("메뉴 그룹이 필요합니다.");
        }

        validatePrice(price, menuProducts);

        Menu menu = new Menu(name, price, menuGroup);
        for (MenuProduct menuProduct : menuProducts) {
            menu.addMenuProduct(menuProduct);
        }

        return menu;
    }

    private static void validatePrice(BigDecimal menuPrice, List<MenuProduct> menuProducts) {
        if (menuPrice.compareTo(totalPriceOf(menuProducts)) > 0) {
            throw new IllegalArgumentException("메뉴 가격은 상품의 총 가격보다 클 수 없습니다.");
        }
    }

    private static BigDecimal totalPriceOf(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> menuProduct.getPrice())
                .reduce(BigDecimal.ZERO, (acc, price) -> acc.add(price));
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
        menuProduct.setMenu(this);
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
}
