package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    @Column
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    private Menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Assert.notNull(name, "이름은 비어있을 수 없습니다.");
        Assert.notNull(price, "가격은 비어있을 수 없습니다.");
        Assert.notNull(menuGroupId, "메뉴 그룹 ID는 비어있을 수 없습니다.");
        Assert.notEmpty(menuProducts, "메뉴 상품 목록은 비어있을 수 없습니다.");

        menuProducts.forEach(menuProduct -> menuProduct.updateMenu(this));

        this.id = id;
        this.name = Name.of(name);
        this.price = Price.of(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = MenuProducts.of(menuProducts);
    }

    public static Menu of(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroupId, menuProducts);
    }

    public static Menu of(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(null, name, price, menuGroupId, menuProducts);
    }

    public boolean isEqualMenuId(Long menuId) {
        return this.id.equals(menuId);
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
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }
}
