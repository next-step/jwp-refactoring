package kitchenpos.menu.domain;

import kitchenpos.common.Price;

import javax.persistence.*;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @Embedded
    private Price price;
    @ManyToOne
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    protected Menu() {}

    public Menu(Long id, String name, Price price, MenuGroup menuGroup) {
        this(name, price, menuGroup);
        this.id = id;
    }

    public Menu(String name, Price price, MenuGroup menuGroup) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Price getPrice() {
        return price;
    }
    public MenuGroup getMenuGroup() {
        return menuGroup;
    }
}
