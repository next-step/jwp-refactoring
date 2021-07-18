package kitchenpos.menu.domain;

import kitchenpos.menu.exception.IllegalPriceException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    @ManyToOne
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    private static final String BASIC_PRICE_EXCEPTION = "금액은 0보다 작거나 null일 수 없습니다.";
    private static final String PRICE_LIMIT_EXCEPTION = "금액은 메뉴 상품들의 총 가격 보다 클 수 없습니다.";

   protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validatePrice(price);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = new MenuProducts(menuProducts);
    }

    public Menu(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalPriceException(BASIC_PRICE_EXCEPTION);
        }
    }

    public void validateLimitPrice(long sumPrice) {
        if (price.compareTo(BigDecimal.valueOf(sumPrice)) > 0) {
            throw new IllegalPriceException(PRICE_LIMIT_EXCEPTION);
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
        return new ArrayList<>(menuProducts.getMenuProducts());
    }

    public List<Long> toProductIds() {
       return menuProducts.toMenuProductIds();
    }
}
