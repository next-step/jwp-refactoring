package kitchenpos.menu.domain;

import kitchenpos.common.Money;
import kitchenpos.menugroup.domain.MenuGroup;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Menu {

    private static final int MIN_PRODUCT_COUNT = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Money price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(final String name, final Money price, final MenuGroup menuGroup, final MenuProducts menuProducts) {
        validate(menuProducts, price);
        menuProducts.assign(this);
        this.name = Objects.requireNonNull(name);
        this.price = Objects.requireNonNull(price);
        this.menuGroup = Objects.requireNonNull(menuGroup);
        this.menuProducts = Objects.requireNonNull(menuProducts);
    }

    private void validate(final MenuProducts menuProducts, final Money price) {
        if (menuProducts.size() < MIN_PRODUCT_COUNT) {
            throw new IllegalArgumentException(String.format("상품은 최소 %d개 이상이어야 합니다.", MIN_PRODUCT_COUNT));
        }

        Money priceOfMenuProducts = menuProducts.price();
        if (price.compareTo(priceOfMenuProducts) > 0) {
            throw new IllegalArgumentException("메뉴 가격은 메뉴 상품들 가격의 합보다 높을 수 없습니다.");
        }
    }
}
