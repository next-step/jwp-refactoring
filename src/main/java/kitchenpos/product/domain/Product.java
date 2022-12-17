package kitchenpos.product.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Embedded
    private Price price;

    protected Product() {

    }

    public Product(String name, Price price) {
        this.name = name;
        this.price = price;
    }

    public static Product of(String name, long price) {
        return new Product(name, Price.of(price));
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Price getPrice() {
        return this.price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (!Objects.equals(id, product.id)) return false;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
