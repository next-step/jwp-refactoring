package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(MenuRequest menuRequest) {
        MenuGroup menuGroup = findMenuGroupById(menuRequest.getMenuGroupId());
        List<MenuProduct> menuProducts = createMenuProducts(menuRequest);
        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuProducts);
        Menu saveMenu = menuRepository.save(menu);
        return MenuResponse.of(saveMenu);
    }

    private MenuGroup findMenuGroupById(Long id) {
        return menuGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("메뉴그룹이 존재하지 않습니다."));
    }

    private List<MenuProduct> createMenuProducts(MenuRequest menuRequest) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        List<Long> productIds = getProductIds(menuRequest.getMenuProducts());
        List<Product> products = productRepository.findAllById(productIds);
        addMenuProduct(menuRequest.getMenuProducts(), menuProducts, products);
        return menuProducts;
    }

    private void addMenuProduct(List<MenuProductRequest> menuProductRequests, List<MenuProduct> menuProducts,
                                List<Product> products) {
        menuProductRequests.forEach(menuProductRequest -> {
                    Product product = findProductById(products, menuProductRequest);
                    menuProducts.add(new MenuProduct(null, product, menuProductRequest.getQuantity()));
                });
    }

    private List<Long> getProductIds(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
    }

    private Product findProductById(List<Product> products, MenuProductRequest menuProductRequest) {
        return products.stream()
                .filter(tempProduce -> tempProduce.getId().equals(menuProductRequest.getProductId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }
}
