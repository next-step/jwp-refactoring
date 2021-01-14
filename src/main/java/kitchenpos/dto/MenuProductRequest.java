package kitchenpos.dto;

public class MenuProductRequest {

	private long seq;
	private long productId;
	private long quantity;

	public MenuProductRequest() {
	}

	public MenuProductRequest(long seq, long productId, long quantity) {
		this.seq = seq;
		this.productId = productId;
		this.quantity = quantity;
	}

	public long getSeq() {
		return seq;
	}

	public long getProductId() {
		return productId;
	}

	public long getQuantity() {
		return quantity;
	}
}
