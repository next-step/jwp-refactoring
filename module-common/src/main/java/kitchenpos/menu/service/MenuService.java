package kitchenpos.menu.service;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final MenuProductService menuProductService;

    public MenuService(MenuRepository menuRepository, MenuGroupService menuGroupService, MenuProductService menuProductService) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.menuProductService = menuProductService;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        checkProductsEmpty(menuRequest);
        final MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());
        final Menu menu = menuRepository.save(Menu.of(menuRequest.getName(), menuRequest.ofMoney(), menuGroup));
        menuRequest.getMenuProducts().forEach(menuProductRequest -> menu.add(menuProductService.saveProduct(menu.getId(), menuProductRequest)));
        menu.checkAllowProductsPrice();
        return MenuResponse.ofMenu(menu);
    }

    private void checkProductsEmpty(final MenuRequest menuRequest) {
        if (menuRequest.isEmptyProducts()) {
            throw new IllegalArgumentException("상품이 비어있습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findFetchJoinAll()
                .stream()
                .map(MenuResponse::ofMenu)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Menu findById(long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을수 없습니다."));
    }

}
