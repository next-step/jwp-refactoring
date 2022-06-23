package kitchenpos.menu.application;

import kitchenpos.core.domain.Amount;
import kitchenpos.core.domain.Price;
import kitchenpos.core.domain.Quantity;
import kitchenpos.core.exception.InvalidPriceException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.NotFoundMenuGroupException;
import kitchenpos.product.exception.NotFoundProductException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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
    public MenuResponse create(final MenuRequest request) {
        validateCreateMenu(request);
        return MenuResponse.of(createMenu(request));
    }

    private void validateCreateMenu(MenuRequest request) {
        validateExistMenuGroup(request.getMenuGroupId());
        validateAmount(new Price(request.getPrice()), request.getMenuProducts());
    }

    private Menu createMenu(MenuRequest request) {
        Menu menu = menuRepository.save(request.toMenu());
        menu.addMenuProducts(toMenuProducts(request));
        return menu;
    }

    private void validateExistMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new NotFoundMenuGroupException();
        }
    }

    private void validateAmount(Price price, List<MenuProductRequest> menuProductRequests) {
        Amount amount = calculateTotalAmount(menuProductRequests);
        if (price.toAmount().isGatherThan(amount)) {
            throw new InvalidPriceException("상품들 금액의 합이 메뉴 가격보다 클 수 없습니다.");
        }
    }

    private Amount calculateTotalAmount(List<MenuProductRequest> menuProductRequests) {
        Amount totalAmount = Amount.ZERO;
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = findProductById(menuProductRequest.getProductId());
            Amount amount = calculateAmount(product.getPrice(), new Quantity(menuProductRequest.getQuantity()));
            totalAmount = totalAmount.add(amount);
        }
        return totalAmount;
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                                .orElseThrow(NotFoundProductException::new);
    }

    private Amount calculateAmount(Price price, Quantity quantity) {
        return price.multiply(quantity);
    }

    private List<MenuProduct> toMenuProducts(MenuRequest request) {
        return request.getMenuProducts()
                      .stream()
                      .map(MenuProductRequest::toMenuProduct)
                      .collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        return MenuResponse.of(menuRepository.findAll());
    }
}
