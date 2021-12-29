package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;

@Service
@Transactional
public class MenuService {

	private final MenuRepository menuRepository;
	private final MenuGroupService menuGroupService;
	private final ProductRepository productRepository;

	public MenuService(final MenuRepository menuRepository,
		final MenuGroupService menuGroupService,
		ProductRepository productRepository) {

		this.menuRepository = menuRepository;
		this.menuGroupService = menuGroupService;
		this.productRepository = productRepository;
	}

	public MenuResponse create(final MenuRequest request) {
		MenuGroup menuGroup = menuGroupService.getById(request.getMenuGroupId());
		Menu menu = request.toEntity(menuGroup);
		List<MenuProduct> menuProductList = getMenuProductList(request.getMenuProducts());
		menu.addMenuProducts(menuProductList);
		menu.checkOverPrice();
		return new MenuResponse(menuRepository.save(menu));
	}

	private List<MenuProduct> getMenuProductList(List<MenuProductRequest> menuProductRequests) {
		return menuProductRequests.stream().map(this::getMenuProduct)
			.collect(Collectors.toList());
	}

	private MenuProduct getMenuProduct(MenuProductRequest request) {
		Product product = productRepository.findById(request.getProductId())
			.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "해당 상품을 찾을 수 없습니다"));
		return request.toEntity(product);
	}

	@Transactional(readOnly = true)
	public List<MenuResponse> list() {
		List<Menu> menuList = menuRepository.findAll();
		return menuList.stream().map(MenuResponse::new).collect(Collectors.toList());
	}

	public Menu getById(Long menuId) {
		return menuRepository.findById(menuId)
			.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "해당 메뉴를 찾을 수 없습니디다"));
	}
}
