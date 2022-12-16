package kitchenpos.menu.application;

import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private static final String ERROR_MESSAGE_NOT_FOUND_MENU_GROUP_FORMAT = "메뉴 그룹을 찾을 수 없습니다. ID : %d";
    private static final String ERROR_MESSAGE_NOT_FOUND_PRODUCT_FORMAT = "상품을 찾을 수 없습니다. ID : %d";

    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuService(MenuRepository menuRepository, ProductRepository productRepository, MenuGroupRepository menuGroupRepository) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        List<MenuProduct> menuProducts = toMenuProduct(request.getMenuProductRequests());
        Menu menu = Menu.of(request.getName(), request.getPrice(), validateMenuGroup(request.getMenuGroupId()), menuProducts);
        return MenuResponse.from(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return MenuResponse.toList(menuRepository.findAll());
    }

    private List<MenuProduct> toMenuProduct(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(request -> MenuProduct.of(findProductById(request.getProductId()), request.getQuantity()))
                .collect(Collectors.toList());
    }

    private long validateMenuGroup(Long id) {
        MenuGroup menuGroup = menuGroupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_MENU_GROUP_FORMAT, id)));
        return menuGroup.id();
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ERROR_MESSAGE_NOT_FOUND_PRODUCT_FORMAT, id)));
    }
}
