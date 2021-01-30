package kitchenpos.menu.application;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductService productService;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        validate(request);
        Menu menu = new Menu(
                request.getName(),
                Price.of(request.getPrice()),
                request.getMenuGroupId(),
                createMenuProducts(request.getMenuProducts()));
        return MenuResponse.of(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    public boolean exists(List<Long> menuIds) {
        return menuRepository.existsByIdIn(menuIds);
    }

    private void validate(MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        List<MenuProductRequest> menuProductsRequest = request.getMenuProducts();
        Price sum = productService.getSumPrice(menuProducts(menuProductsRequest));
        if (request.getPrice() > sum.intValue()) {
            throw new IllegalArgumentException();
        }
    }

    private List<Long> menuProducts(List<MenuProductRequest> menuProductsRequest) {
        return menuProductsRequest.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
    }

    private List<MenuProduct> createMenuProducts(List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
                .map(product -> new MenuProduct(productService.findById(product.getProductId()), product.getQuantity()))
                .collect(Collectors.toList());
    }

    public Menu findById(long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴 입니다."));
    }
}
