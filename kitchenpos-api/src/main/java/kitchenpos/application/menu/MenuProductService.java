package kitchenpos.application.menu;

import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuProductService {
	private final MenuProductRepository menuProductRepository;
	private final ProductRepository productRepository;

	public MenuProductService(MenuProductRepository menuProductRepository, ProductRepository productRepository) {
		this.menuProductRepository = menuProductRepository;
		this.productRepository = productRepository;
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public MenuProduct saveProduct(long menuId, MenuProductRequest menuProductRequest) {
		return menuProductRepository.save(MenuProduct.of(
				menuId,
				productRepository.findById(menuProductRequest.getProductId()).orElseThrow(() -> new IllegalArgumentException()),
				menuProductRequest.getQuantity()
		));
	}

	public List<MenuProduct> findAllByMenuId(Long id) {
		return menuProductRepository.findAllByMenuId(id);
	}
}
