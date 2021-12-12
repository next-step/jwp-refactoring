package kitchenpos.menu.domain;

import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import org.springframework.util.Assert;

@Entity
public class Menu {

    private static final int MIN_PRODUCTS_SIZE = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @ManyToOne(optional = false)
    @JoinColumn(name = "menu_group_id", updatable = false, nullable = false, foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    private Menu(Name name, Price price, MenuGroup menuGroup, List<MenuProduct> products) {
        MenuProducts menuProducts = MenuProducts.from(products);
        validate(name, price, menuGroup, menuProducts);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        menuProducts.changeMenu(this);
        this.menuProducts = menuProducts;
    }

    public static Menu of(Name name, Price price, MenuGroup menuGroup, List<MenuProduct> products) {
        return new Menu(name, price, menuGroup, products);
    }

    public Long id() {
        return id;
    }

    public Name name() {
        return name;
    }

    public Price price() {
        return price;
    }

    public MenuGroup menuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> menuProducts() {
        return menuProducts.list();
    }

    private void validate(Name name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        Assert.notNull(name, "이름은 필수입니다.");
        Assert.notNull(price, "가격은 필수입니다.");
        Assert.notNull(menuGroup, "메뉴 그룹은 필수입니다.");

        Assert.notNull(menuProducts, "메뉴 상품들은 필수입니다.");
        Assert.isTrue(menuProducts.size() >= MIN_PRODUCTS_SIZE,
            String.format("메뉴 상품들(%s)은 적어도 %d개 이상이어야 합니다.", menuProducts, MIN_PRODUCTS_SIZE));
        Price productsSumPrice = menuProducts.sumPrice();
        Assert.isTrue(price.equalOrLessThan(productsSumPrice),
            String.format("메뉴 가격(%s)은 메뉴 상품들(%s)의 가격보다 작거나 같아야 합니다.",
                price, productsSumPrice));
    }
}
