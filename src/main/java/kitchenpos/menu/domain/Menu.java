package kitchenpos.menu.domain;

import kitchenpos.common.Name;
import kitchenpos.common.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
    public static final String MENU_GROUP_NOT_NULL_EXCEPTION_MESSAGE = "메뉴 그룹이 없을 수 없습니다.";
    public static final String PRICE_NOT_NULL_EXCEPTION_MESSAGE = "가격은 필수입니다.";
    public static final String MENU_PRICE_EXCEPTION_MESSAGE = "메뉴의 가격이 메뉴 상품의 합보다 클 수 없다.";

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
    private MenuProducts menuProducts = new MenuProducts();

    public Menu(Name name, Price price, Long menuGroupId, List<MenuProduct> menuProducts) {
        if (Objects.isNull(menuGroupId)) {
            throw new IllegalArgumentException(MENU_GROUP_NOT_NULL_EXCEPTION_MESSAGE);
        }
        if (Objects.isNull(price)) {
            throw new IllegalArgumentException(PRICE_NOT_NULL_EXCEPTION_MESSAGE);
        }
        BigDecimal sum = BigDecimal.ZERO;
        if (menuProducts.isEmpty()) {
            throw new IllegalArgumentException("메뉴 상품이 없습니다.");
        }
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getProduct().getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.getPrice().compareTo(sum) > 0) {
            throw new IllegalArgumentException(MENU_PRICE_EXCEPTION_MESSAGE);
        }
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts.addAll(menuProducts);
    }

    public Menu(long id, Name name, Price price, MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name.getName();
    }

    public BigDecimal getPrice() {
        return this.price.getPrice();
    }

    public List<MenuProduct> getMenuProducts() {
        return this.menuProducts.getMenuProducts();
    }
}
