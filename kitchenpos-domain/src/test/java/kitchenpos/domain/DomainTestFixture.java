package kitchenpos.domain;

public class DomainTestFixture {
	public static final long 예제_테이블_그룹_ID = 1L;

	public static OrderTable 그룹_지정된_테이블_객체() {
		OrderTable table = OrderTable.create();
		table.saveGroupInfo(1L);
		return table;
	}

	public static OrderTable 비어있지않은_테이블_객체() {
		OrderTable table = OrderTable.create();
		table.changeEmpty(false);
		return table;
	}

	public static OrderTable 빈_테이블_객체() {
		return OrderTable.create();
	}

	public static OrderTable 빈_테이블_객체1() {
		return OrderTable.create();
	}

	public static OrderTable 빈_테이블_객체2() {
		return OrderTable.create();
	}
	public static Order 일반_주문() {
		return Order.create(비어있지않은_테이블_객체());
	}

	public static Order 완료된_주문() {
		Order order = 일반_주문();
		order.changeOrderStatus(OrderStatus.COMPLETION);
		return order;
	}
}
