package kitchenpos.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "menu")
public class MenuEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroupEntity menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected MenuEntity() {
    }

    private MenuEntity(String name, BigDecimal price, MenuGroupEntity menuGroup) {
        this.name = name;
        this.price = new Price(price);
        this.menuGroup = menuGroup;
    }

    public static MenuEntity createMenu(
            String name,
            BigDecimal price,
            MenuGroupEntity menuGroup,
            List<MenuProductEntity> menuProducts
    ) {
        if (menuGroup == null) {
            throw new IllegalArgumentException("메뉴 그룹이 필요합니다.");
        }

        validatePrice(price, menuProducts);

        MenuEntity menu = new MenuEntity(name, price, menuGroup);
        for (MenuProductEntity menuProduct : menuProducts) {
            menu.addMenuProduct(menuProduct);
        }

        return menu;
    }

    private static void validatePrice(BigDecimal menuPrice, List<MenuProductEntity> menuProducts) {
        if (menuPrice.compareTo(totalPriceOf(menuProducts)) > 0) {
            throw new IllegalArgumentException("메뉴 가격은 상품의 총 가격보다 클 수 없습니다.");
        }
    }

    private static BigDecimal totalPriceOf(List<MenuProductEntity> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> menuProduct.getPrice())
                .reduce(BigDecimal.ZERO, (acc, price) -> acc.add(price));
    }

    public void addMenuProduct(MenuProductEntity menuProduct) {
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

    public MenuGroupEntity getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProductEntity> getMenuProducts() {
        return menuProducts.getElements();
    }
}
