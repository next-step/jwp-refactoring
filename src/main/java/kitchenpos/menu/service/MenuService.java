package kitchenpos.menu.service;

import kitchenpos.generic.Money;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.service.MenuGroupService;
import kitchenpos.menuproduct.service.MenuProductService;
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
        final Menu menu = menuRepository.save(new Menu(menuRequest.getName(), Money.price(menuRequest.getPrice()), menuGroup));
        List<MenuProductResponse> menuProductResponses = menuProductService.saveProducts(menu, menuRequest.getMenuProducts());
        return MenuResponse.ofMenu(menu, menuProductResponses);
    }

    private void checkProductsEmpty(final MenuRequest menuRequest) {
        if (menuRequest.isEmptyProducts()) {
            throw new IllegalArgumentException("상품이 비어있습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(menu -> MenuResponse.ofMenu(menu, menuProductService.list(menu)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Menu findById(long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을수 없습니다."));
    }

}
