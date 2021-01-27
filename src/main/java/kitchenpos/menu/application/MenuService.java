package kitchenpos.menu.application;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
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
        Menu save = menuRepository.save(createMenu(request));
        return MenuResponse.of(save);
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

    private Menu createMenu(MenuRequest request) {
        Menu menu = new Menu(request.getName(), Price.of(request.getPrice()), request.getMenuGroupId());
        List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(product -> new MenuProduct(productService.findById(product.getProductId()), product.getQuantity()))
                .collect(Collectors.toList());
        menu.addProducts(menuProducts);
        return menu;

//        Menu menu = new Menu(request.getName(), Price.of(request.getPrice()), request.getMenuGroupId());
//        List<MenuProduct> menuProducts = findMenuProducts(request.getMenuProducts());
//        menu.addProducts(menuProducts);
//
//        return menu;
    }

//    private List<MenuProduct> findMenuProducts(List<MenuProductRequest> menuProductRequests) {
//        List<Long> ids = menuProductRequests.stream()
//                .map(MenuProductRequest::getProductId)
//                .collect(Collectors.toList());
//
//        List<Product> products = productService.findAllByIdIn(ids);
//        return products.stream()
//                .map(it -> new MenuProduct(it, findRequestQuntity(menuProductRequests, it.getId())))
//                .collect(Collectors.toList());
//    }
//
//    private long findRequestQuntity(List<MenuProductRequest> menuProductRequests, Long id) {
//        return menuProductRequests.stream()
//                .filter(it -> it.getProductId().equals(id))
//                .findFirst()
//                .orElseThrow(IllegalArgumentException::new)
//                .getQuantity();
//    }
}
