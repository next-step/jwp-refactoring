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

import org.springframework.util.CollectionUtils;

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
        return menuProducts.findAll();
    }

    public void updateMenuProducts(MenuProducts menuProducts) {
        menuProducts.updateMenu(this);
        this.menuProducts = menuProducts;
    }

    public static class Builder {
        private Name name;
        private Price price;
        private MenuGroup menuGroup;
        private MenuProducts menuProducts;

        private Long id;

        public Builder(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
            this.name = new Name(name);
            this.price = new Price(price);
            this.menuGroup = menuGroup;
            this.menuProducts = checkValidMenuProducts(menuProducts);
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        private MenuProducts checkValidMenuProducts(List<MenuProduct> menuProducts) {
            if (CollectionUtils.isEmpty(menuProducts)) {
                throw new IllegalArgumentException("메뉴에는 1개 이상의 상품이 포함되어야합니다.");
            }
            return new MenuProducts(menuProducts);
        }

        public Menu build() {
            return new Menu(this);
        }
    }

    private Menu (Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.price = builder.price;
        this.menuGroup = builder.menuGroup;
        updateMenuProducts(builder.menuProducts);
    }
}
