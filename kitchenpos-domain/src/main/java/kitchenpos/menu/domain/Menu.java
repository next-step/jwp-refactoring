package kitchenpos.menu.domain;



import kitchenpos.menu.dto.MenuProductResponse;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    @Embedded
    private final MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        validateLessThanZero(price);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    private void validateLessThanZero(BigDecimal price) {
        if (BigDecimal.ZERO.compareTo(price) > 0) {
            throw new IllegalArgumentException("가격이 0보다 적을 수 없습니다.");
        }
    }

    public void add(List<MenuProduct> menuProduct) {
        menuProducts.add(this.id, menuProduct);
    }

    public void addAllMenuProducts(List<MenuProduct> menuProducts) {
        validatePriceCompareToSum(menuProducts);
        add(menuProducts);
    }

    private void validatePriceCompareToSum(List<MenuProduct> menuProducts) {
        BigDecimal sum = menuProducts.stream()
                .map(MenuProduct::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴 가격이 메뉴별 상품의 총 가격보다 클 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProductResponse> getMenuProductResponses() {
        return menuProducts.getMenuProductResponses();
    }
}
