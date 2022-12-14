package kitchenpos.ui.dto;

public class ProductRequest {

	private String name;
	private Long price;

	private ProductRequest() {
	}

	public ProductRequest(String name, Long price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public Long getPrice() {
		return price;
	}
}
