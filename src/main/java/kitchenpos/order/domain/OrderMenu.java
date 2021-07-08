package kitchenpos.order.domain;

import static java.util.Objects.*;

import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;

@Embeddable
public class OrderMenu {

	@Column(name = "menu_id", nullable = false)
	private Long id;

	@AttributeOverride(name = "value", column = @Column(name = "menu_name", nullable = false))
	private Name name;

	@AttributeOverride(name = "amount", column = @Column(name = "menu_price", nullable = false))
	private Price price;

	protected OrderMenu() { }

	private OrderMenu(Long id, Name name, Price price) {
		validateNonNull(id, name, price);
		this.id = id;
		this.name = name;
		this.price = price;
	}

	private void validateNonNull(Long menuId, Name menuName, Price menuPrice) {
		if (isNull(menuId) || isNull(menuName) || isNull(menuPrice)) {
			throw new IllegalArgumentException("주문메뉴 필수정보가 부족합니다.");
		}
	}

	public static OrderMenu of(Long menuId, Name menuName, Price menuPrice) {
		return new OrderMenu(menuId, menuName, menuPrice);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		OrderMenu orderMenu = (OrderMenu)o;
		return Objects.equals(id, orderMenu.id) && Objects.equals(name, orderMenu.name)
			&& Objects.equals(price, orderMenu.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, price);
	}

	public Long getId() {
		return id;
	}
}
