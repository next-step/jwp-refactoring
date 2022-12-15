package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(MenuRepository menuRepository, MenuGroupService menuGroupService,
            ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public Menu create(final Menu menu) {
        menuGroupService.existsById(menu.getMenuGroupId());
        productService.existProducts(menu.productList());
        menu.checkValidPrice();
        menu.setMenuToMenuProducts();
        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

    public void validMenuCount(List<Long> menuIds) {
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("요청한 메뉴 갯수와 저장된 메뉴 갯수가 일치하지 않습니다");
        }
    }
}
