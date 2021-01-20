package kitchenpos.domain;

import kitchenpos.exception.BadPriceException;

import javax.persistence.*;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long price;
    @ManyToOne
    private MenuGroup menuGroup;
    @Embedded
    private final MenuProducts menuProducts = new MenuProducts();

    protected Menu(){}

    public Menu(String name, Long price, MenuGroup menuGroup) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public void add(Product product, Long quantity) {
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

    public Long getPrice() {
        return price;
    }

    public long getMenuGroupId() {
        return menuGroup.getId();
    }
}
