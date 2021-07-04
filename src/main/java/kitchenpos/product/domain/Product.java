package kitchenpos.product.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kitchenpos.common.domian.Price;
import kitchenpos.product.dto.ProductResponse;

@Entity
@Table(name = "product")
public class Product {

    public Product() {}

    private Product(String name, Price price) {
        this.name = name;
        this.price = price;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Product of(String name, int price) {
        return new Product(name, new Price(price));
    }

    public Price getPrice() {
        return price;
    }

    public ProductResponse toResponse() {
        return ProductResponse.of(id, name, price.amountToInt());
    }
}
