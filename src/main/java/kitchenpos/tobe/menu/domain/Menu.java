package kitchenpos.tobe.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.tobe.common.domain.Name;
import kitchenpos.tobe.common.domain.Price;

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
        final MenuValidator menuValidator
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
        final MenuValidator menuValidator
    ) {
        this(null, name, price, menuProducts, menuGroupId, menuValidator);
    }

    public boolean isOverpriced() {
        return price.compareTo(menuProducts.calculateTotalPrice()) > COMPARISON_EQUAL_TO;
    }
}
