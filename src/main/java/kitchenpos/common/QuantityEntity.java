package kitchenpos.common;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class QuantityEntity {

	private static final long MIN_QUANTITY = 1;

	private Long quantity;

	protected Long validate(Long quantity) {
		if(quantity < MIN_QUANTITY){
			throw new IllegalArgumentException("수량은 1보다 크거나 같아야합니다.");
		}
		return quantity;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
}
