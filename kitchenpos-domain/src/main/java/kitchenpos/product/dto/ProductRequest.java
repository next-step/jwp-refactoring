package kitchenpos.product.dto;

import java.math.BigDecimal;

import kitchenpos.product.domain.Product;

public class ProductRequest {

	private ProductRequest() {
	}

	public static class Create {
		private String name;
		private BigDecimal price;

		private Create() {
		}

		public Product toEntity() {
			return Product.of(name, price);
		}

		public Create(String name, BigDecimal price) {
			this.name = name;
			this.price = price;
		}

		public String getName() {
			return name;
		}

		public BigDecimal getPrice() {
			return price;
		}
	}

}
