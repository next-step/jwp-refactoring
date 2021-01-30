package kitchenpos.menu.domain.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import kitchenpos.menu.domain.product.Product;
import kitchenpos.menu.dto.menu.MenuProductRequest;

@Embeddable
public class MenuProducts {

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuProduct> menuProducts = new ArrayList<>();

	protected MenuProducts() {
	}

	private MenuProducts(final Menu menu, final List<Product> products,
		final List<MenuProductRequest> menuProductRequests) {
		List<MenuProduct> menuProducts = toMenuProducts(menu, products, menuProductRequests);
		this.menuProducts.addAll(menuProducts);
	}

	public MenuProducts(final List<MenuProduct> menuProducts) {
		this.menuProducts.addAll(menuProducts);
	}

	public static MenuProducts create(final Menu menu, final List<Product> products,
		final List<MenuProductRequest> menuProductRequests) {
		return new MenuProducts(menu, products, menuProductRequests);
	}
	private List<MenuProduct> toMenuProducts(final Menu menu, final List<Product> products,
		final List<MenuProductRequest> menuProductRequests) {
		Map<Long, Product> productById = getProductMap(products);

		validateProductsRequestExists(productById, menuProductRequests);

		return menuProductRequests.stream()
			.filter(menuProduct -> productById.containsKey(menuProduct.getProductId()))
			.map(menuProduct -> {
				Product product = productById.get(menuProduct.getProductId());
				return new MenuProduct(menu, product, menuProduct.getQuantity());
			})
			.collect(Collectors.toList());
	}

	private Map<Long, Product> getProductMap(final List<Product> products) {
		return products.stream()
			.collect(Collectors.toMap(Product::getId, Function.identity()));
	}

	public BigDecimal totalPrice() {
		return BigDecimal.valueOf(this.menuProducts.stream()
			.map(MenuProduct::getPrice)
			.mapToLong(BigDecimal::longValue)
			.sum());
	}

	public List<MenuProduct> getMenuProducts() {
		return this.menuProducts;
	}

	private void validateProductsRequestExists(final Map<Long, Product> productById,
		final List<MenuProductRequest> menuProductRequests) {
		menuProductRequests.stream()
			.filter(menuProductRequest -> !productById.containsKey(menuProductRequest.getProductId()))
			.findAny()
			.ifPresent(menuProductRequest -> {
				throw new IllegalArgumentException(String.format("요청한 %d에 대한 상품 정보가 없습니다.",
					menuProductRequest.getProductId()));
			});
	}
}
