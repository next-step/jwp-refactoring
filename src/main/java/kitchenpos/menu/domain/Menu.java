package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu() {
    }

    private Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validate(menuGroup, price, menuProducts);

        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;

        if(menuProducts != null) {
            for (MenuProduct menuProduct : menuProducts) {
                menuProduct.changeMenu(this);
            }
        }
    }

    private void validate(MenuGroup menuGroup, BigDecimal price, List<MenuProduct> menuProducts) {
        if(menuGroup == null) {
            throw new IllegalArgumentException("메뉴는 특정 메뉴 그룹에 속해야 합니다");
        }
        if(menuProducts == null || menuProducts.isEmpty()) {
            return;
        }
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴 가격은 0 이상이어야 합니다.");
        }
        BigDecimal sum = menuProducts.stream()
            .map(MenuProduct::getSumPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴 가격은 메뉴에 속한 상품들의 금액보다 클 수 없습니다.");
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

    public static final class Builder {
        private Long id;
        private String name;
        private BigDecimal price;
        private MenuGroup menuGroup;
        private List<MenuProduct> menuProducts = new ArrayList<>();

        public Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder menuGroup(MenuGroup menuGroup) {
            this.menuGroup = menuGroup;
            return this;
        }

        public Builder menuProducts(MenuProduct... menuProducts) {
            this.menuProducts = Arrays.asList(menuProducts);
            return this;
        }

        public Builder menuProducts(List<MenuProduct> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public Menu build() {
            return new Menu(id, name, price, menuGroup, menuProducts);
        }
    }
}
