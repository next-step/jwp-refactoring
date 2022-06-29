package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menugroup.domain.MenuGroup;

@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price) {
        this.name = new Name(name);
        this.price = new Price(price);
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this.name = new Name(name);
        this.price = new Price(price);
        this.menuGroup = menuGroup;
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
        this.id = id;
        this.name = new Name(name);
        this.price = new Price(price);
        this.menuGroup = menuGroup;
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = new Name(name);
        this.price = new Price(price);
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public void addMenuProduct(List<MenuProduct> source) {
        validateAmount(source);
        menuProducts.addAll(source);
    }

    private void validateAmount(List<MenuProduct> source) {
        Price sum = source.stream().
                map(MenuProduct::calculateAmount).
                reduce(new Price(BigDecimal.ZERO), Price::add);
        if (price.greaterThan(sum)) {
            throw new IllegalArgumentException("상품 가격의 합계 보다 비싼 메뉴 가격을 추가 할 수 업습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public void registerMenuGroup(MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

}
