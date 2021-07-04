package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.util.CollectionUtils;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.domain.MenuGroup;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price"))
    private Price price;

    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {}

    public Menu(String name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(Long id, String name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        this.id = id;
        this.menuGroupId = menuGroupId;
        this.name = name;
        this.price = price;
        this.menuProducts = menuProducts;
        menuProducts.assginMenu(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = Price.of(price);
    }

    public BigDecimal getPrice() {
        return price.toBigDecimal();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.toCollection();
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = MenuProducts.of(menuProducts);
    }

    public static Menu create(MenuRequest menuRequest, MenuGroup menuGroup, MenuProducts menuProducts) {
        validationCreate(menuRequest, menuProducts);
        return new Menu(menuRequest.getName(), Price.of(menuRequest.getPrice()), menuGroup.getId(), menuProducts);
    }

    private static void validationCreate(MenuRequest menuRequest, MenuProducts menuProducts) {
        if (CollectionUtils.isEmpty(menuProducts.toCollection())) {
            throw new IllegalArgumentException();
        }

        if (menuProducts.isSumUnder(menuRequest.getPrice())) {
            throw new IllegalArgumentException();
        }
    }

}
