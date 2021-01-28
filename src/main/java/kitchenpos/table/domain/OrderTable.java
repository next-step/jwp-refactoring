package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

@Entity
public class OrderTable {
	private static int MIN_NUMBER_OF_GUESTS = 0;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long tableGroupId;
	private int numberOfGuests;
	private boolean empty;
	@OneToMany
	@JoinColumn(name = "orderTableId")
	private List<Order> orders = new ArrayList<>();

	public OrderTable() {
	}

	public OrderTable(Long id, int numberOfGuests, boolean empty,
		List<Order> orders) {
		this.id = id;
		this.numberOfGuests = numberOfGuests;
		this.empty = empty;
		this.orders = orders;
	}

	public Long getId() {
		return id;
	}

	public Long getTableGroupId() {
		return tableGroupId;
	}

	public boolean isInGroup() {
		return tableGroupId != null && tableGroupId != 0;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public boolean isEmpty() {
		return empty;
	}

	public void changeEmpty(boolean empty) {
		if (getTableGroupId() != null && getTableGroupId() != 0) {
			throw new IllegalArgumentException("빈 테이블 여부를 수정하기 위해서는 단체가 지정되어 있지 않아야 합니다.");
		}

		if (isExistNoCompletionOrder()) {
			throw new IllegalArgumentException("빈 테이블 여부를 수정하기 위해서는 모든 주문 상태가 계산 완료여야 합니다.");
		}
		this.empty = empty;
	}

	public void changeNumberOfGuests(int numberOfGuests) {
		if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
			throw new IllegalArgumentException("방문한 손님 수는 " + MIN_NUMBER_OF_GUESTS + " 보다 작을 수 없습니다.");
		}
		if (empty) {
			throw new IllegalArgumentException("빈 테이블은 방문한 손님 수를 수정할 수 없습니다.");
		}
		this.numberOfGuests = numberOfGuests;
	}

	public void ungroup() {
		if (isExistNoCompletionOrder()) {
			throw new IllegalArgumentException("단체 지정을 해제하기 위해서는 주문 테이블의 주문 상태가 모두 계산 완료여야 합니다.");
		}
		this.tableGroupId = null;
	}

	private boolean isExistNoCompletionOrder() {
		return Optional.ofNullable(orders)
			.orElseGet(Collections::emptyList)
			.stream()
			.map(Order::getOrderStatus)
			.anyMatch(it -> OrderStatus.COOKING.equals(it) || OrderStatus.MEAL.equals(it));
	}

	public static final class Builder {
		private Long id;
		private int numberOfGuests;
		private boolean empty;
		private List<Order> orders = new ArrayList<>();

		public Builder() {
		}

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder numberOfGuests(int numberOfGuests) {
			this.numberOfGuests = numberOfGuests;
			return this;
		}

		public Builder empty(boolean empty) {
			this.empty = empty;
			return this;
		}

		public Builder orders(List<Order> orders) {
			this.orders = orders;
			return this;
		}

		public Builder orders(Order... orders) {
			this.orders = Arrays.asList(orders);
			return this;
		}

		public OrderTable build() {
			return new OrderTable(id, numberOfGuests, empty, orders);
		}
	}
}
