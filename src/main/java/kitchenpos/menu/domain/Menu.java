package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;
    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
        this.id = id;
        this.name = new Name(name);
        this.price = new Price(price);
        this.menuGroup = menuGroup;
    }

    public Menu(String name, BigDecimal price) {
        this.name = new Name(name);
        this.price = new Price(price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.value();
    }

    public BigDecimal getPrice() {
        return price.value();
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.list();
    }

    public void updateMenuGroup(MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

    public void updateMenuProducts(MenuProducts menuProducts) {
        checkPriceExpensiveThanProductsPriceSum(menuProducts);
        menuProducts.updateMenu(this);
        this.menuProducts = menuProducts;
    }

    private void checkPriceExpensiveThanProductsPriceSum(MenuProducts menuProducts) {
        if (price.isExpensiveThan(menuProducts.priceSum())) {
            throw new IllegalArgumentException("메뉴의 가격이 메뉴의 속하는 상품 가격의 합보다 비쌉니다.");
        }
    }
}
