package ktichenpos.menu.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MenuProductRequest {

	private final long productId;
	private final long quantity;

    @JsonCreator
    public MenuProductRequest(
        @JsonProperty("productId") long productId,
        @JsonProperty("quantity") long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

	public long getProductId() {
		return productId;
	}

	public long getQuantity() {
		return quantity;
	}
}
