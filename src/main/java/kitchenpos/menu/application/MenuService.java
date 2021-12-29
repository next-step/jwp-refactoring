package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupService menuGroupService,
            final ProductService productService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        MenuGroup menuGroup = menuGroupService.findById(request.getMenuGroupId());
        
        Menu menu = request.toMenu(menuGroup);
        
        MenuProducts menuProducts = createMenuProducts(request.getMenuProducts());
        menu.addMenuProducts(menuProducts.getMenuProducts());
        
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
    
    @Transactional(readOnly = true)
    private MenuProducts createMenuProducts(List<MenuProductRequest> request) {
        List<MenuProduct> result = new ArrayList<MenuProduct>();
        
        List<Long> productIds = request.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
        
        List<Product> products = productService.findAllByIds(productIds);
        
        if (products.size() != request.size()) {
            new IllegalArgumentException("메뉴에는 저장된 상품만 등록할 수 있습니다");
        }
        
        for (int i = 0; i < products.size(); i++) {
            result.add(MenuProduct.of(products.get(i), request.get(i).getQuantity()));
        }

        return MenuProducts.from(result);
    }
    
    @Transactional(readOnly = true)
    public List<Menu> findAllByIds(List<Long> ids) {
        return menuRepository.findAllByIds(ids);
    }
}
