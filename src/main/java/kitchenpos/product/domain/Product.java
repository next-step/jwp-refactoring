package kitchenpos.product.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.exception.InvalidPriceException;
import kitchenpos.product.dto.ProductResponse;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Long price;

    protected Product() {
    }

    public Product(String name, Long price) {
        this(null, name, price);
    }

    public Product(Long id, String name, Long price) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validatePrice(Long price) {
        if (price == null || price < 0) {
            throw new InvalidPriceException();
        }
    }

    public Long getId() {
        return id;
    }

    public Long getPrice() {
        return price;
    }

    public ProductResponse toProductResponse() {
        return new ProductResponse(this.id, this.name, this.price);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name)
                && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}
