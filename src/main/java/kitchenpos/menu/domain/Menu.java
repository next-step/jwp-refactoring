package kitchenpos.menu.domain;

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
    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    private Menu(MenuBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.price = builder.price;
        this.menuGroup = builder.menuGroup;
        this.menuProducts = builder.menuProducts;
    }

    public static MenuBuilder builder() {
        return new MenuBuilder();
    }

    public Long getMenuGroupId() {
        if(Objects.isNull(menuGroup)){
            return null;
        }
        return menuGroup.getId();
    }

    public static class MenuBuilder {
        private Long id;
        private String name;
        private BigDecimal price;
        private MenuGroup menuGroup;
        private List<MenuProduct> menuProducts;

        public MenuBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MenuBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MenuBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public MenuBuilder menuGroup(MenuGroup menuGroup) {
            this.menuGroup = menuGroup;
            return this;
        }

        public MenuBuilder menuProducts(List<MenuProduct> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public Menu build() {
            return new Menu(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public MenuGroup getMenuGroup(){
        return menuGroup;
    }

    public void setMenuGroup(final MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
