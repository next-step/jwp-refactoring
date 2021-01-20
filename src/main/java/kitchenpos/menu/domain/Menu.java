package kitchenpos.menu.domain;

import kitchenpos.common.Price;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    public Menu() {
    }

    private Menu(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.price = builder.price;
        this.menuGroup = builder.menuGroup;
        updateMenuProducts(builder.menuProducts);
    }

    public void updateMenuProducts(MenuProducts menuProducts) {
        menuProducts.updateMenu(this);
        this.menuProducts = menuProducts;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.value();
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.findAll();
    }

    public static class Builder {
        private Long id;
        private String name;
        private Price price;
        private MenuGroup menuGroup;
        private MenuProducts menuProducts;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = new Price(price);
            return this;
        }

        public Builder menuGroup(MenuGroup menuGroup) {
            this.menuGroup = menuGroup;
            return this;
        }

        public Builder menuProducts(List<MenuProduct> menuProducts) {
            this.menuProducts = checkValidMenuProducts(menuProducts);
            return this;
        }

        public Menu build() {
            return new Menu(this);
        }

        private MenuProducts checkValidMenuProducts(List<MenuProduct> menuProducts) {
            if (CollectionUtils.isEmpty(menuProducts)) {
                throw new IllegalArgumentException("메뉴에는 1개 이상의 상품이 포함되어야 합니다.");
            }
            return new MenuProducts(menuProducts);
        }
    }

}