package kitchenpos.menu.domain;

import kitchenpos.menu.exception.IllegalPriceException;
import kitchenpos.menugroup.domain.MenuGroup;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroup menuGroup;

    @OneToMany
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, Long sum, List<MenuProduct> menuProducts) {
        validatePrice(price);
        this.name = name;
        this.price = price;
        validateLimitPrice(sum);
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalPriceException("금액은 0보다 작거나 null일 수 없습니다.");
        }
    }

    public void validateLimitPrice(long sumPrice) {
        if (price.compareTo(BigDecimal.valueOf(sumPrice)) > 0) {
            throw new IllegalPriceException("금액은 메뉴 상품들의 총 가격 보다 클 수 없습니다.");
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
