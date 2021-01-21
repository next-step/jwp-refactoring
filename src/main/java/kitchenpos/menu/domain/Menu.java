package kitchenpos.menu.domain;

import kitchenpos.menuGroup.domain.MenuGroup;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(Long id) {
        this(id, BigDecimal.ZERO, MenuGroup.empty(), Collections.emptyList());
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this(name, price, menuGroup, Collections.emptyList());
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {

        this.name = name;
        this.price = validationCheck(price);
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        checkPrice();
    }

    public Menu(Long id, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.id = id;
        this.price = validationCheck(price);
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        checkPrice();
    }

    public void initialMenuProduct() {
        menuProducts.forEach(menuProduct -> menuProduct.addMenu(this));
    }

    public BigDecimal validationCheck(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("입력된 가격이 올바르지 않습니다.");
        }
        return price;
    }

    public void checkPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.calculatePrice());
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("상품가격 총합과 메뉴의 가격이 올바르지 않습니다.");
        }
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
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
