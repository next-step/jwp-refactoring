package kitchenpos.menu.application;

import kitchenpos.advice.exception.MenuException;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.product.application.ProductService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.domain.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final ProductService productService;
    private final MenuGroupService menuGroupService;

    public MenuService(
        final MenuRepository menuRepository,
        final ProductService productService,
        final MenuGroupService menuGroupService) {
        this.menuRepository = menuRepository;
        this.productService = productService;
        this.menuGroupService = menuGroupService;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());
        Menu menu = getMenu(menuRequest, menuGroup);
        return menu;
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

    public long countByIdIn(List<Long> ids) {
        return menuRepository.countByIdIn(ids);
    }

    public Menu findById(long id) {
        return menuRepository.findById(id).orElseThrow(() -> new MenuException("메뉴가 존재하지 않습니다", id));
    }

    public List<Menu> findAllById(List<Long> ids) {
        return menuRepository.findAllById(ids);
    }

    public Menu getMenu(MenuRequest menuRequest, MenuGroup menuGroup){
        Menu menu = menuRepository.save(menuRequest.toMenu(menuGroup));

        List<Product> products = productService.findAllById(menuRequest.getProductIds());
        Map<Long, Product> productMap = products.stream()
            .collect(Collectors.toMap(Product::getId, product -> product));

        for (MenuProductRequest request : menuRequest.getMenuProducts()) {
            MenuProduct menuProduct = new MenuProduct(productMap.get(request.getProductId()), request.getQuantity());
            menu.addMenuProduct(menuProduct);
        }

        return menu;
    }
}
