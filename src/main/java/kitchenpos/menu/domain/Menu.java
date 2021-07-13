package kitchenpos.menu.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.menu.exception.MenuException;

@Entity
@Table(name = "menu")
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(length = 20, nullable = false)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Embedded
	private Price price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"), nullable = false)
	private MenuGroup menuGroup;

	protected Menu() {

	}

	public Menu(String name, Price price, MenuGroup menuGroup) {
		validate(price, menuGroup);
		this.name = name;
		this.price = price;
		this.menuGroup = menuGroup;
	}

	public Menu(long id, String name, Price price, MenuGroup menuGroup) {
		this(name, price, menuGroup);
		this.id = id;
	}

	private void validate(Price price, MenuGroup menuGroup) {
		validateMenuGroup(menuGroup);

	}

	private void validateMenuGroup(MenuGroup menuGroup) {
		if (Objects.isNull(menuGroup)) {
			throw new MenuException("메뉴 그룹이 비워져 있습니다.");
		}
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

	public MenuGroup getMenuGroup() {
		return menuGroup;
	}

}
