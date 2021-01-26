package kitchenpos.menu.application;

import common.application.NotFoundException;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
	static final String MSG_CANNOT_FIND_PRODUCT = "Cannot find Product by productId";
	static final String MSG_CANNOT_FIND_MENUGROUP = "Cannot find MenuGroup by menuGroupId";

	private final MenuRepository menuRepository;
	private final MenuGroupRepository menuGroupRepository;
	private final ProductRepository productRepository;

	public MenuService(final MenuRepository menuRepository,
	                   final MenuGroupRepository menuGroupRepository,
	                   final ProductRepository productRepository) {
		this.menuRepository = menuRepository;
		this.menuGroupRepository = menuGroupRepository;
		this.productRepository = productRepository;
	}

	@Transactional
	public MenuResponse create(MenuRequest menuRequest) {
		MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
				.orElseThrow(() -> new NotFoundException(MSG_CANNOT_FIND_MENUGROUP));
		Menu menu = menuRequest.createMenu(menuGroup);

		List<Product> products = getProducts(menuRequest.getProductIds());
		menu.addMenuProducts(menuRequest.createMenuProducts(products));
		return MenuResponse.of(menuRepository.save(menu));
	}

	private List<Product> getProducts(List<Long> productIds) {
		List<Product> products = productRepository.findAllById(productIds);
		if (productIds.size() != products.size()) {
			throw new NotFoundException(MSG_CANNOT_FIND_PRODUCT);
		}

		return products;
	}

	public List<MenuResponse> list() {
		List<Menu> menus = menuRepository.findAllWithMenuGroupFetchJoin();
		return menus.stream()
				.map(MenuResponse::of)
				.collect(Collectors.toList());
	}
}
