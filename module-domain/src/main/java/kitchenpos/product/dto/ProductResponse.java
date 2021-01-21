package kitchenpos.product.dto;

import kitchenpos.product.Product;

public class ProductResponse {
    private Long id;
    private String name;
    private Long price;

    public ProductResponse(final Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice().longValue();
    }

    public long getId() {
        return id;
    }

    public ProductResponse setId(final long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductResponse setName(final String name) {
        this.name = name;
        return this;
    }

    public long getPrice() {
        return price;
    }

    public ProductResponse setPrice(final long price) {
        this.price = price;
        return this;
    }
}
