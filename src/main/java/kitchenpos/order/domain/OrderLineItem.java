package kitchenpos.order.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.common.domain.Quantity;
import kitchenpos.common.exception.BadRequestException;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long menuId;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(Long id, Long menuId, long quantity) {
        validateMenuId(menuId);
        this.id = id;
        this.menuId = menuId;
        this.quantity = new Quantity(quantity);
    }

    private void validateMenuId(Long menuId) {
        if (Objects.isNull(menuId)) {
            throw new BadRequestException(WRONG_VALUE);
        }
    }

    public static OrderLineItem of(Long menuId, long quantity) {
        return new OrderLineItem(null, menuId, quantity);
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
