package kitchenpos.ordertablegroup.domain;

public class Order {
	private OrderStatus orderStatus;

	private Order() {
	}

	public static Order from(String orderStatus) {
		Order order = new Order();
		order.orderStatus = OrderStatus.valueOf(orderStatus);
		return order;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public enum OrderStatus {
		COOKING, MEAL, COMPLETION;

		public boolean isCompletion() {
			return this == COMPLETION;
		}
	}
}
