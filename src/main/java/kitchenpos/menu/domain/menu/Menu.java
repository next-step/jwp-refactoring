package kitchenpos.menu.domain.menu;

import kitchenpos.menu.domain.Price;
import kitchenpos.menu.domain.menugroup.MenuGroup;
import kitchenpos.menu.domain.menuproduct.MenuProducts;
import kitchenpos.menu.domain.product.Product;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price menuPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(String name, BigDecimal menuPrice, MenuGroup menuGroup) {
        validation(name, menuPrice, menuGroup);
        this.name = name;
        this.menuPrice = new Price(menuPrice);
        this.menuGroup = menuGroup;
    }

    private void validation(String name, BigDecimal menuPrice, MenuGroup menuGroup) {
        if (ObjectUtils.isEmpty(name)) {
            throw new IllegalArgumentException("메뉴 명이 없습니다.");
        }
        if (menuPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴 금액이 잘못되었습니다.");
        }
        if (Objects.isNull(menuGroup)) {
            throw new IllegalArgumentException("메뉴 그룹이 없습니다.");
        }
    }

    public void addMenuProduct(Product product, long quantity) {
        menuProducts.add(this, product, quantity);
    }

    public boolean isPriceLt(BigDecimal price) {
        return this.menuPrice.lt(price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getMenuPrice() {
        return menuPrice;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}
