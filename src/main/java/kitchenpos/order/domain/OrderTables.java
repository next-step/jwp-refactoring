package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {
	@OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderTable> orderTables = new ArrayList<>();

	protected OrderTables() {
	}

	public OrderTables(List<OrderTable> orderTables) {
		this.orderTables = orderTables;

		validate();
	}

	private void validate() {
		if (CollectionUtils.isEmpty(orderTables)) {
			throw new IllegalArgumentException("테이블 정보가 없습니다.");
		}
	}

	public boolean isEmpty() {
		return CollectionUtils.isEmpty(orderTables);
	}

	public boolean isLessThan(int size) {
		return orderTables.size() < size;
	}

	public List<OrderTable> getList() {
		return Collections.unmodifiableList(orderTables);
	}

	public List<Long> getIds() {
		return orderTables.stream()
			.map(OrderTable::getId)
			.collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
	}
}
