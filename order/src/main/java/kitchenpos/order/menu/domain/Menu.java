package kitchenpos.order.menu.domain;

import java.math.BigDecimal;

import javax.persistence.Embedded;

import org.springframework.util.Assert;

import kitchenpos.common.domain.Price;

public class Menu {
	private Long id;

	private String name;

	@Embedded
	private Price price;

	public Menu() {
	}

	private Menu(long id, String name, Price price) {
		Assert.notNull(name, "메뉴 이름은 필수입니다.");
		Assert.notNull(price, "메뉴 가격은 필수입니다.");
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public static Menu of(long id, String name, Price price) {
		return new Menu(id, name, price);
	}

	public Long id() {
		return id;
	}

	public String name() {
		return name;
	}

	public BigDecimal price() {
		return price.value();
	}

}
