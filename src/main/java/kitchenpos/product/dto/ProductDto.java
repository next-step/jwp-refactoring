package kitchenpos.product.dto;

import java.math.BigDecimal;

import kitchenpos.product.domain.Product;

public class ProductDto {
	private Long id;
	private String name;
	private BigDecimal price;

	public ProductDto() {
	}

	public ProductDto(Long id, String name, BigDecimal price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public static ProductDto from(Product product) {
		ProductDto dto = new ProductDto();
		dto.id = product.getId();
		dto.name = product.getName().getValue();
		dto.price = product.getPrice().getValue();
		return dto;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}
}
