package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final  ProductService productService;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository, ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productService = productService;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {

        MenuGroup menuGroup  = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                                                    .orElseThrow(() -> new IllegalArgumentException("메뉴그룹이 등록되어있지 않습니다."));

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);

        final List<MenuProductRequest> menuProducts = menuRequest.getMenuProducts();

        menuProducts.stream()
                    .map(menuProductRequest -> new MenuProduct(null, productService.getProduct(menuProductRequest.getProductId()), menuProductRequest.getQuantity()))
                    .forEach(menu::addMenuProduct);

        menu.getMenuProducts().checkOverPrice(menuRequest.getPrice());

        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
