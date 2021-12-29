package kitchenpos.menu.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.common.domain.Price;

@Entity
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "bigint(20)")
	private Long id;

	@Column(nullable = false)
	private String name;

	@Embedded
	@Column(length = 19, scale = 2, nullable = false)
	private Price price;

	@Column(name = "menuGroupId", columnDefinition = "bigint(20)", nullable = false)
	private Long menuGroupId;

	public Menu() {
	}

	public Menu(Long id) {
		this.id = id;
	}

	public Menu(String name, Price price, Long menuGroupId) {
		this(null, name, price, menuGroupId);
	}

	public Menu(Long id, String name, Price price, Long menuGroupId) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.menuGroupId = menuGroupId;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Price getPrice() {
		return price;
	}

	public Long getMenuGroupId() {
		return menuGroupId;
	}

	public BigDecimal getPriceValue() {
		return price.value();
	}
}
