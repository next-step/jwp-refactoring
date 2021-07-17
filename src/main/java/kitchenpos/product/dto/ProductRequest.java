package kitchenpos.product.dto;

import java.math.BigDecimal;

import kitchenpos.menu.domain.Price;
import kitchenpos.product.domain.Product;

public class ProductRequest {
    private Long id;
    private String name;
    // private BigDecimal price;
	private Long price;

    public ProductRequest() {
	}

	public ProductRequest(String name, Long price) {
    	this.name = name;
    	this.price = price;
	}

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(final Long price) {
        this.price = price;
    }

    public Product toProduct() {
		return new Product(name, new Price(new BigDecimal(price)));
	}
}
