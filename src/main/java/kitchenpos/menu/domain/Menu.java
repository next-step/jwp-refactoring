package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import org.springframework.util.Assert;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
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

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    private Menu(Long id, Name name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        Assert.notNull(name, "이름은 비어있을 수 없습니다.");
        Assert.notNull(price, "가격은 비어있을 수 없습니다.");
        Assert.notNull(menuGroup, "메뉴 그룹은 비어있을 수 없습니다.");
        Assert.notNull(menuProducts, "메뉴 상품 목록은 비어있을 수 없습니다.");

        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static Menu of(Long id, Name name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        return new Menu(id, name, price, menuGroup, menuProducts);
    }

    public static Menu of(Name name, Price price, MenuGroup menuGroup) {
        return new Menu(null, name, price, menuGroup, new MenuProducts());
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        menuProduct.updateMenu(this);
        menuProducts.add(menuProduct);
    }

    public void checkOverPrice() {
        if (menuProducts.isOverPrice(price.getPrice())) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }
}
