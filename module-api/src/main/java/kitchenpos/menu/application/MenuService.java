package kitchenpos.menu.application;

import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductService productService;


    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository, ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {

        MenuGroup menuGroup  = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                                                    .orElseThrow(() -> new IllegalArgumentException("메뉴그룹이 등록되어있지 않습니다."));

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);

        final List<MenuProductRequest> menuProducts = menuRequest.getMenuProducts();

        menuProducts.stream()
                .map(menuProductRequest -> new MenuProduct(menu, productService.getProduct(menuProductRequest.getProductId()), menuProductRequest.getQuantity()))
                .forEach(menu::addMenuProduct);

        menu.getMenuProducts().checkOverPrice(menuRequest.getPrice());

        return MenuResponse.from(menuRepository.save(menu));
    }
    @Transactional(readOnly=true)
    public List<MenuResponse> list() {

        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());

    }
}
