package kitchenpos.product.domain;

import static kitchenpos.exception.ProductPriceEmptyException.PRODUCT_PRICE_EMPTY_EXCEPTION;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.Price;
import kitchenpos.common.Quantity;


@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private ProductName name;
    @Embedded
    private Price price;

    public Product() {
    }

    public Product(Long id, ProductName name, Price price) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(ProductName name, Price price) {
        validatePrice(price);
        this.name = name;
        this.price = price;
    }

    public Price priceByQuantity(Quantity quantity) {
        return price.multiply(quantity);
    }

    private void validatePrice(Price price) {
        if (price == null) {
            throw PRODUCT_PRICE_EMPTY_EXCEPTION;
        }
    }

    public Long getId() {
        return id;
    }

    public ProductName getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public boolean isEqualToId(Long productId) {
        return id.equals(productId);
    }
}
