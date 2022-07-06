package kitchenpos.menu.application;

import kitchenpos.global.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class MenuService {
    private static final int FIRST_INDEX = 0;
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuGroupService menuGroupService;

    public MenuService(
            final MenuRepository menuRepository,
            final ProductRepository productRepository,
            final MenuGroupService menuGroupService
    ) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
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
        validPrice(request);
    }

    public boolean validateMenuGroupId(Long menuGroupId) {
        return menuGroupService.existsById(menuGroupId);
    }

    private void validateMenuProduct(MenuRequest request) {
        List<Long> productIds = request.getMenuProductRequests().stream()
                .map(MenuProductRequest::getProductId)
                .collect(toList());

        List<Product> products = productRepository.findByIdIn(productIds);

        if (productIds.size() != products.size()) {
            throw new IllegalArgumentException("등록되지 않은 상품이 있습니다.");
        }
    }

    private void validPrice(MenuRequest request) {
        Price total = new Price(BigDecimal.ZERO);
        for (MenuProductRequest menuProductRequest : request.getMenuProductRequests()) {
            Product product = findByProductId(menuProductRequest.getProductId());
            Price menuProductPrice = new Price(product.getMultipleValue(menuProductRequest.getQuantity()));
            total = total.add(menuProductPrice);
        }

        if (total.getValue().compareTo(request.getPrice()) > 0) {
            throw new IllegalArgumentException("메뉴 상품보다 메뉴의 가격이 높을 수 없습니다.");
        }
    }

    private Product findByProductId(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 상품이 있습니다."));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::of)
                .collect(toList());
    }

    private List<Product> getProducts(MenuRequest request) {
        List<Long> productIds = request.getMenuProductRequests().stream()
                .map(MenuProductRequest::getProductId)
                .collect(toList());

        List<Product> products = productRepository.findByIdIn(productIds);
        if (products.size() != request.getMenuProductRequests().size()) {
            throw new IllegalArgumentException("등록되지 않은 상품이 있습니다.");
        }

        return products;
    }

    public Menu findById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 메뉴입니다."));
    }
}
