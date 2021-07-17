package kitchenpos.product.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;

@Entity
public class Product {
	private Long id;

	private String name;

	private BigDecimal price;
}
