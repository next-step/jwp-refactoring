package kitchenpos.order.domain;

public interface OrderTables {
	boolean contains(Long id);

	OrderTable findById(Long id);
}
