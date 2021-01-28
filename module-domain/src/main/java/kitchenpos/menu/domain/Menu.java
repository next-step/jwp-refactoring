package kitchenpos.menu.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(Long id) {
        this(BigDecimal.ZERO, MenuGroup.empty(), MenuProducts.empty());
        this.id = id;
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this(price, menuGroup, MenuProducts.empty());
        this.name = name;
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this(price, menuGroup, menuProducts);
        this.name = name;
    }

    public Menu(BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.price = validationCheck(price);
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        checkPrice();
    }

    public void checkPrice() {
        BigDecimal sum = menuProducts.calculateTotalPrice();

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("상품가격 총합과 메뉴의 가격이 올바르지 않습니다.");
        }
    }

    private BigDecimal validationCheck(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("입력된 가격이 올바르지 않습니다.");
        }
        return price;
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

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id) &&
                Objects.equals(name, menu.name) &&
                Objects.equals(price, menu.price) &&
                Objects.equals(menuGroup, menu.menuGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroup);
    }
}
