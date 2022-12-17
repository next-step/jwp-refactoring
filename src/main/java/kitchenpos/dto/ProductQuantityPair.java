package kitchenpos.dto;

import com.fasterxml.jackson.annotation.JsonGetter;

import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.domain.Quantity;

public class ProductQuantityPair {
    private Product product;
    private Quantity quantity;

    public ProductQuantityPair() {
    }

    public ProductQuantityPair(Product product, Quantity quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Price sumOfPrice() {
        return product.getPrice().getTotalPrice(quantity);
    }

    @JsonGetter("quantity")
    public Long quantity() {
        return quantity.value();
    }
}
