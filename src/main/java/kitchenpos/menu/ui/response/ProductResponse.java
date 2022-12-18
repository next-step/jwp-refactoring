package kitchenpos.menu.ui.response;

public class ProductResponse {

	private final Long id;
	private final String name;
	private final Long price;

	private ProductResponse(Long id, String name, Long price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public static ProductResponse of(Long id, String name, Long price) {
		return new ProductResponse(id, name, price);
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
