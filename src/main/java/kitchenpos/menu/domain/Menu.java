package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private BigDecimal price;

	@ManyToOne
	@JoinColumn(name = "menu_group_id")
	private MenuGroup menuGroup;

	public Menu() {
	}

	public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
		validate(price);
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
	}
	private void validate(BigDecimal price) {
		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("가격은 0보다 큰 숫자여야 합니다.");
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public MenuGroup getMenuGroup() {
		return menuGroup;
	}
}
