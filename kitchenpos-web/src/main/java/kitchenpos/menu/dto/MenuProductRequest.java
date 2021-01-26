package kitchenpos.menu.dto;

/**
 * @author : byungkyu
 * @date : 2021/01/26
 * @description :
 **/
public class MenuProductRequest {
	private Long productId;
	private long quantity;

	public MenuProductRequest() {
	}

	public MenuProductRequest(Long productId, long quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	public Long getProductId() {
		return productId;
	}

	public long getQuantity() {
		return quantity;
	}
}
