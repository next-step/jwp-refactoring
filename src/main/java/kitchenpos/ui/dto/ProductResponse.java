package kitchenpos.ui.dto;

public class ProductResponse {

	private Long id;
	private String name;
	private Long price;

	private ProductResponse() {
	}

	public ProductResponse(Long id, String name, Long price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Long getPrice() {
		return price;
	}
}
