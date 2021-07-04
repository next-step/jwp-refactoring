package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {}

    public Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public Menu(Long id, String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.id = id;
        this.menuGroup = menuGroup;
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public void setMenuGroup(MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.toCollection();
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = MenuProducts.of(menuProducts);
    }

    public static Menu create(MenuRequest menuRequest, MenuGroup menuGroup, MenuProducts menuProducts) {
        validationCreate(menuRequest, menuProducts);
        return new Menu(menuRequest.getName(), Price.of(menuRequest.getPrice()), menuGroup, menuProducts);
    }

    private static void validationCreate(MenuRequest menuRequest, MenuProducts menuProducts) {
        if (CollectionUtils.isEmpty(menuProducts.toCollection())) {
            throw new IllegalArgumentException();
        }

        if (menuProducts.isSumUnder(menuRequest.getPrice())) {
            throw new IllegalArgumentException();
        }
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

}
