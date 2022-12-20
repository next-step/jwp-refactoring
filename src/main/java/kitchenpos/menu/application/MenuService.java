package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
import kitchenpos.menu.ui.response.MenuResponse;

@Service
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuGroupRepository menuGroupRepository;
	private final ProductRepository productRepository;

	public MenuService(
		MenuRepository menuRepository,
		MenuGroupRepository menuGroupRepository,
		ProductRepository productRepository
	) {
		this.menuRepository = menuRepository;
		this.menuGroupRepository = menuGroupRepository;
		this.productRepository = productRepository;
	}

	@Transactional
	public MenuResponse create(final MenuRequest request) {
		final BigDecimal price = request.getPrice();

		if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException();
		}

		if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
			throw new IllegalArgumentException();
		}

		final List<MenuProductRequest> menuProducts = request.getMenuProductRequests();

		BigDecimal sum = BigDecimal.ZERO;
		for (final MenuProductRequest menuProduct : menuProducts) {
			final Product product = productRepository.findById(menuProduct.getProductId())
				.orElseThrow(IllegalArgumentException::new);
			sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
		}

		if (price.compareTo(sum) > 0) {
			throw new IllegalArgumentException();
		}

		return MenuResponse.from(menuRepository.save(request.toEntity()));
	}

	public List<MenuResponse> list() {
		return MenuResponse.listFrom(menuRepository.findAll());
	}
}
