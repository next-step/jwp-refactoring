package kitchenpos.product.domain;

import java.util.List;
import java.util.Objects;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    private Menu(Name name, Price price, MenuGroup menuGroup, MenuProducts products) {
        validate(name, price, menuGroup, products);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        products.changeMenu(this);
        this.menuProducts = products;
    }

    public static Menu of(Name name, Price price, MenuGroup menuGroup, List<MenuProduct> products) {
        return new Menu(name, price, menuGroup, MenuProducts.from(products));
    }

    public long id() {
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
        Assert.isTrue(menuProducts.isNotEmpty(), "메뉴 상품들은 비어있을 수 없습니다.");

        Price productsSumPrice = menuProducts.sumPrice();
        Assert.isTrue(price.equalOrLessThan(productsSumPrice),
            String.format("메뉴 가격(%s)은 메뉴 상품들(%s)의 가격보다 작거나 같아야 합니다.",
                price, productsSumPrice));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menu menu = (Menu) o;
        return id == menu.id && Objects.equals(name, menu.name) && Objects
            .equals(price, menu.price);
    }

    @Override
    public String toString() {
        return "Menu{" +
            "id=" + id +
            ", name=" + name +
            ", price=" + price +
            ", menuGroup=" + menuGroup +
            '}';
    }
}
