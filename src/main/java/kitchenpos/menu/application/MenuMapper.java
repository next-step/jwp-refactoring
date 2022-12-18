package kitchenpos.menu.application;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.ui.dto.MenuRequest;

@Service
public class MenuMapper {

	private final ProductService productService;

	public MenuMapper(ProductService productService) {
		this.productService = productService;
	}

	public Menu toMenu(MenuRequest menuRequest) {
		List<Product> products = productService.findAllById(menuRequest.toProductsId());
		return new Menu(menuRequest.getName(),
						menuRequest.getPrice(),
						menuRequest.getMenuGroupId(),
						multiplyQuantity(menuRequest, products));
	}

	private Map<Product, Integer> multiplyQuantity(MenuRequest menuRequest, List<Product> products) {
		Map<Long, Integer> productsCount = menuRequest.toProducts();
		return products.stream()
					   .collect(Collectors.toMap(
						   Function.identity(),
						   product -> productsCount.get(product.getId()),
						   Integer::sum
					   ));
	}

}
