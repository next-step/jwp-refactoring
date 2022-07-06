package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final ProductService productService;
    private final MenuGroupService menuGroupService;

    public MenuService(
            final MenuRepository menuRepository,
            final ProductService productService,
            final MenuGroupService menuGroupService
    ) {
        this.menuRepository = menuRepository;
        this.productService = productService;
        this.menuGroupService = menuGroupService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        validateCreate(request);
        Menu menu = request.toEntity();
        Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.of(savedMenu);
    }

    private void validateCreate(MenuRequest request) {
        validateMenuGroupId(request.getMenuGroupId());
        validateMenuProduct(request);
    }

    public void validateMenuGroupId(Long menuGroupId) {
        if (!menuGroupService.existsById(menuGroupId)) {
            new IllegalArgumentException("등록되지 않은 메뉴가 있습니다.");
        }
    }

    private void validateMenuProduct(MenuRequest request) {
        final long sum = request.getMenuProductRequests()
                .stream()
                .map(menuProduct -> productService.findById(menuProduct.getProductId()).getPrice().longValue()
                                * menuProduct.getQuantity())
                .reduce(Long::sum)
                .get();
        if (request.getPrice().longValue() > sum) {
            throw new IllegalArgumentException("메뉴의 가격은 상품 가격의 총합보다 클 수 없습니다.");
        }
    }

    private Product findByProductId(Long productId) {
        return productService.findById(productId);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::of)
                .collect(toList());
    }

    public Menu findById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 메뉴입니다."));
    }
}
