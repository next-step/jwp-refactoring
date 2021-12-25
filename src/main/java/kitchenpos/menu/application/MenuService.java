package kitchenpos.menu.application;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository,
                       MenuGroupRepository menuGroupRepository,
                       ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = findMenuGroupById(menuRequest.getMenuGroupId());
        final List<MenuProduct> menuProducts = getMenuProducts(menuRequest);
        final Menu menu = Menu.of(menuRequest.getName(), Price.of(menuRequest.getPrice()), menuGroup);
        menu.addMenuProducts(menuProducts);

        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.of(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    private MenuGroup findMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new NotFoundException("해당 메뉴 그룹을 찾을 수 없습니다."));
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품을 찾을 수 없습니다."));
    }

    private List<MenuProduct> getMenuProducts(MenuRequest menuRequest) {
        final List<MenuProduct> menuProductList = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuRequest.getMenuProductRequests()) {
            Product product = findProductById(menuProductRequest.getProductId());
            Quantity quantity = Quantity.of(menuProductRequest.getQuantity());

            menuProductList.add(MenuProduct.of(product, quantity));
        }
        return menuProductList;
    }
}
