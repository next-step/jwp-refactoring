package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.orders.domain.Orders;

/**
 * @author : byungkyu
 * @date : 2021/01/25
 * @description :
 **/
@Embeddable
public class OrderCollections {
	@OneToMany(mappedBy = "orderTable", cascade = CascadeType.ALL)
	private List<Orders> orderCollections = new ArrayList<>();

	public OrderCollections() {
	}

	public OrderCollections(List<Orders> orderCollections) {
		this.orderCollections = orderCollections;
	}

	public List<Orders> getOrderCollections() {
		return orderCollections;
	}

	public boolean isStatusCompletion() {
		return orderCollections.stream()
			.anyMatch(Orders::isStatusCompletion);
	}
}
