package kitchenpos.menus.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Validator;

@Entity
public class Menu {

    private final static int COMPARISON_EQUAL_TO = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @Embedded
    private MenuProducts menuProducts;

    @Column(name = "menu_group_id", nullable = false)
    private Long menuGroupId;

    protected Menu() {
    }

    public Menu(
        final Long id,
        final Name name,
        final Price price,
        final MenuProducts menuProducts,
        final Long menuGroupId,
        final Validator<Menu> menuValidator
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuProducts = menuProducts;
        this.menuGroupId = menuGroupId;

        menuValidator.validate(this);
    }

    public Menu(
        final Name name,
        final Price price,
        final MenuProducts menuProducts,
        final Long menuGroupId,
        final Validator<Menu> menuValidator
    ) {
        this(null, name, price, menuProducts, menuGroupId, menuValidator);
    }

    public boolean isOverpriced() {
        return price.compareTo(menuProducts.calculateTotalPrice()) > COMPARISON_EQUAL_TO;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.asString();
    }

    public BigDecimal getPrice() {
        return price.asBigDecimal();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.asList();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<Long> getProductIds() {
        return menuProducts.getProductIds();
    }
}
