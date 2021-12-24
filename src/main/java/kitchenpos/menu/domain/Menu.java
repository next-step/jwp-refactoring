package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import org.springframework.util.CollectionUtils;

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
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_group"))
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {
    }

    public Menu(final String name, final BigDecimal price, final MenuGroup menuGroup) {
        this.name = Name.of(name);
        this.price = Price.of(price);
        this.menuGroup = menuGroup;
    }

    public Menu(final Long id, final String name, final Long price, final MenuGroup menuGroup,
        final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = Name.of(name);
        this.price = Price.of(BigDecimal.valueOf(price));
        this.menuGroup = menuGroup;
        this.menuProducts = MenuProducts.of(menuProducts);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public void addMenuProducts(final List<MenuProduct> menuProducts) {
        validateMenuProductsNotEmpty(menuProducts);
        validateNotOverPrice(menuProducts);

        menuProducts.forEach(menuProduct -> this.menuProducts.addMenuProduct(menuProduct));
    }

    private void validateMenuProductsNotEmpty(List<MenuProduct> menuProducts) {
        if (CollectionUtils.isEmpty(menuProducts)) {
            throw new IllegalArgumentException("메뉴 상품은 하나 이상여야 합니다.");
        }
    }

    private void validateNotOverPrice(final List<MenuProduct> menuProducts) {
        BigDecimal totalPrice = menuProducts.stream()
            .map(MenuProduct::getPrice)
            .reduce(BigDecimal::add)
            .orElse(BigDecimal.ZERO);

        if (price.isOverPrice(totalPrice)) {
            throw new IllegalArgumentException("메뉴의 가격이 상품 가격의 총합을 초과했습니다.");
        }
    }
}
