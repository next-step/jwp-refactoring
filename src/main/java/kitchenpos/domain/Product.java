package kitchenpos.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price"))
    private Money price;

    protected Product() {
    }

    public Product(Long id, String name, Money price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(Long id, String name, int price) {
        this(id, name, Money.valueOf(price));
    }

    public Product(String name, Money price) {
        this(null, name, price);
    }

	public Product(String name, int price) {
        this(name, Money.valueOf(price));
	}

	public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }
}
