package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.Products;

@Embeddable
public class MenuProducts {

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<MenuProduct> menuProducts = new ArrayList<>();
	
	public static MenuProducts of(Products products, MenuRequest request) {

		return null;
	}
}
