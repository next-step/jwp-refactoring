package kitchenpos.order.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {

	@OneToMany(mappedBy = "tableGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<OrderTable> orderTables = new ArrayList<>();

	protected OrderTables() {
	}

	public OrderTables(List<OrderTable> orderTables, int requestSize) {
		if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
			throw new IllegalArgumentException("테이블 그룹은 최소 2개 이상의 테이블로 구성되어야 합니다.");
		}
		if (orderTables.size() != requestSize) {
			throw new IllegalArgumentException("요청한 테이블 묶음 개수와 실제 테이블 개수는 같아야합니다.");
		}

		this.orderTables = orderTables;
	}

	public void setOrderTables(List<OrderTable> orderTables) {
		this.orderTables = orderTables;
	}

	public List<OrderTable> getOrderTables() {
		return orderTables;
	}

	public void setTableGroup(TableGroup tableGroup) {
		this.orderTables.forEach(orderTable -> orderTable.setTableGroup(tableGroup));
	}

}