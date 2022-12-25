package kitchenpos.order.order.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;

import org.springframework.util.Assert;

public class OrderLineItemMenu {

	@Column(nullable = false, updatable = false)
	private long menuId;

	@Embedded
	private Price price;

	private String name;

	protected OrderLineItemMenu() {
	}

	private OrderLineItemMenu(long menuId, String name, Price price) {
		Assert.notNull(price, "가격은 필수입니다.");
		Assert.notNull(name, "이름은 필수입니다.");
		this.menuId = menuId;
		this.name = name;
		this.price = price;
	}

	public static OrderLineItemMenu of(long menuId, String name, Price price) {
		return new OrderLineItemMenu(menuId, name, price);
	}

	public long id() {
		return menuId;
	}

	public Price price() {
		return price;
	}

	public String name() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(menuId, price, name);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		OrderLineItemMenu that = (OrderLineItemMenu)o;
		return menuId == that.menuId && Objects.equals(price, that.price) && Objects
			.equals(name, that.name);
	}

	@Override
	public String toString() {
		return "OrderLineItemMenu{" +
			"menuId=" + menuId +
			", price=" + price +
			", name=" + name +
			'}';
	}
}
