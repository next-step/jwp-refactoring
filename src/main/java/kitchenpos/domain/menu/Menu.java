package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;

@Entity
@Table(name = "menu")
public class Menu {

    private static final String INVALID_MENU_PRICE = "메뉴(Menu)의 가격(Price)는 상품(Product)의 총합보다 작아야 합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"), nullable = false)
    private Long menuGroupId;
    @Embedded
    private MenuProducts menuProducts = MenuProducts.createEmpty();

    protected Menu() {}

    private Menu(String name, BigDecimal price, Long menuGroupId) {
        this.name = Name.from(name);
        this.price = Price.from(price);
        this.menuGroupId = menuGroupId;
    }

    private Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = Name.from(name);
        this.price = Price.from(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = MenuProducts.from(menuProducts);
    }

    private Menu(String name, BigDecimal price) {
        this.name = Name.from(name);
        this.price = Price.from(price);
    }

    public static Menu of(String name, BigDecimal price, Long menuGroupId) {
        return new Menu(name, price, menuGroupId);
    }

    public static Menu of(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public static Menu of(String name, BigDecimal price) {
        return new Menu(name, price);
    }

    public Long getId() {
        return id;
    }

    public String findName() {
        return name.getValue();
    }

    public BigDecimal findPrice() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return this.menuGroupId;
    }

    public List<MenuProduct> findMenuProducts() {
        return menuProducts.getReadOnlyValues();
    }

    public void appendMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }

    public void appendAllMenuProducts(List<MenuProduct> menuProducts) {
        validateMenuPrice(menuProducts);
        menuProducts.forEach(menuProduct -> menuProduct.mappedByMenu(this));
    }

    private void validateMenuPrice(List<MenuProduct> menuProducts) {
        BigDecimal sum = menuProducts.stream()
                .map(MenuProduct::calculateTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException(INVALID_MENU_PRICE);
        }
    }
}
