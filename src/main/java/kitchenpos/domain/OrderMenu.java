package kitchenpos.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class OrderMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Order order;
    @ManyToOne
    private Menu menu;
    private Long quantity;

    public OrderMenu(Order order, Menu menu, Long quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menu.getId();
    }
}
