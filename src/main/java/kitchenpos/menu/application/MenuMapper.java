package kitchenpos.menu.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.ui.dto.MenuRequest;

@Service
@Transactional(readOnly = true)
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
			MenuProduct.of(products));
	}

}
