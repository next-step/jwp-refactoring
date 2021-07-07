package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import kitchenpos.exception.CalculationFailedException;
import kitchenpos.exception.ExceedingTotalPriceException;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        checkArguments(name, price, menuGroup, menuProducts);
        checkPriceAndSummation(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        menuProducts.forEach(this::addMenuProduct);
    }

    private void checkArguments(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        if (Objects.isNull(name) || Objects.isNull(price) || Objects.isNull(menuGroup) || Objects.isNull(menuProducts)) {
            throw new IllegalArgumentException("메뉴를 생성하려면 모든 필수값이 입력되어야 합니다.");
        }

        if (menuProducts.isEmpty()) {
            throw new IllegalArgumentException("메뉴 구성요소는 1개 이상 존재해야 합니다.");
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴 가격은 음수가 될 수 없습니다.");
        }
    }

    private void checkPriceAndSummation(BigDecimal price, List<MenuProduct> menuProducts) {
        BigDecimal summation = menuProducts.stream()
            .map(MenuProduct::getTotalPrice)
            .reduce(BigDecimal::add)
            .orElseThrow(() -> new CalculationFailedException("단품 가격의 합계를 계산하지 못했습니다."));

        if (price.compareTo(summation) > 0) {
            throw new ExceedingTotalPriceException("메뉴 가격이 제품 가격의 총 합을 초과합니다.");
        }
    }

    private void addMenuProduct(MenuProduct menuProduct) {
        menuProduct.setMenu(this);
        menuProducts.add(menuProduct);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }

    public BigDecimal getPrice() {
        return price;
    }
}
