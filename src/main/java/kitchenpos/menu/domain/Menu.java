package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Menu {

    private static final String INVALID_PRICE = "유요하지 않은 가격입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Embedded
    private Price price;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    protected Menu() {

    }

    public Menu(String name, int price, MenuGroup menuGroup) {
        this.name = name;
        this.price = new Price(price);
        this.menuGroup = menuGroup;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

}
