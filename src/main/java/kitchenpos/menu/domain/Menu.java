package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Collections;
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
import kitchenpos.menu.exception.NoMenuProductListException;
import kitchenpos.menu.exception.OverSumPriceException;

@Entity
public class Menu {
    public static final Menu EMPTY = new Menu();

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

    protected Menu() {}

    public Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public Menu(Long id, String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.id = id;
        this.menuGroup = menuGroup;
        this.name = name;
        this.price = price;
        this.menuProducts = menuProducts;
    }

    public static Menu create(MenuRequest menuRequest, MenuGroup menuGroup) {
        return new Menu(menuRequest.getName(), Price.of(menuRequest.getPrice()), menuGroup, null);
    }

    private static void validationCreate(MenuRequest menuRequest, MenuProducts menuProducts) {
        if (CollectionUtils.isEmpty(menuProducts.toCollection())) {
            throw new NoMenuProductListException();
        }

        if (menuProducts.isSumUnder(menuRequest.getPrice())) {
            throw new OverSumPriceException();
        }
    }

    public BigDecimal getPrice() {
        return price.toBigDecimal();
    }

    public Long getId() {
        return id;
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public String getName() {
        return name;
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts.toCollection());
    }

    public void productsAssginMenu(MenuRequest menuRequest, MenuProducts menuProducts) {
        validationCreate(menuRequest, menuProducts);
        this.menuProducts = menuProducts;
        menuProducts.assginMenu(id);
    }

}
