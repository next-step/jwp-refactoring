package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupService menuGroupService,
            final MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        MenuGroup menuGroup = menuGroupService.findById(request.getMenuGroupId());
        
        Menu menu = request.toMenu(menuGroup);
        
        MenuProducts menuProducts = createMenuProducts(request.getMenuProducts());
        menu.addMenuProducts(menuProducts.getMenuProducts());
        
        menuValidator.checkTotalPrice(menu);
        
        final Menu savedMenu = menuRepository.save(menu);

        return MenuResponse.from(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
    
    private MenuProducts createMenuProducts(List<MenuProductRequest> request) {
        List<MenuProduct> result = new ArrayList<MenuProduct>();
        
        List<Long> productIds = request.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
        menuValidator.checkProducts(productIds);
        
        for (MenuProductRequest menuProductRequest : request) {
            result.add(MenuProduct.of(menuProductRequest.getProductId(), menuProductRequest.getQuantity()));
        }

        return MenuProducts.from(result);
    }
    
    @Transactional(readOnly = true)
    public List<Menu> findAllByIds(List<Long> ids) {
        return menuRepository.findAllByIdIn(ids);
    }
}
