package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.exception.NotFoundException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
public class MenuService {
	private final MenuRepository menuRepository;
	private final MenuGroupRepository menuGroupRepository;
	private final ProductRepository productRepository;

	public MenuService(
		final MenuRepository menuRepository,
		final MenuGroupRepository menuGroupRepository,
		final ProductRepository productRepository
	) {
		this.menuRepository = menuRepository;
		this.menuGroupRepository = menuGroupRepository;
		this.productRepository = productRepository;
	}

	@Transactional
	public MenuResponse create(final MenuRequest menuRequest) {

		final List<Long> productIds = menuRequest.getProductIds();
		List<Product> products = productRepository.findAllById(productIds);

		final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
			.orElseThrow(() -> new NotFoundException("메뉴 그룹 정보를 찾을 수 없습니다."));

		final Menu savedMenu = menuRepository.save(Menu.create(menuRequest.getName(), menuRequest.getPrice(), menuGroup, products, menuRequest.getQuantities()));

		return MenuResponse.of(savedMenu);
	}

	public List<MenuResponse> findAll() {
		final List<Menu> menus = menuRepository.findAllFetch();
		return menus.stream()
			.map(MenuResponse::of)
			.collect(Collectors.toList());
	}
}
