package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.exception.BadPriceException;

import javax.persistence.*;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private long price;
    @ManyToOne
    private MenuGroup menuGroup;
    @Embedded
    private final MenuProducts menuProducts = new MenuProducts();

    protected Menu(){}

    public Menu(String name, long price, MenuGroup menuGroup) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public void add(Product product, long quantity) {
        this.menuProducts.add(new MenuProduct(this, product, quantity));
        if (this.menuProducts.sumTotalPrice() < price) {
            throw new BadPriceException("메뉴의 가격은 전체 상품 가격보다 높을 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public long getMenuGroupId() {
        return menuGroup.getId();
    }
}
