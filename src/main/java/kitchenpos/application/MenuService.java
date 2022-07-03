package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                                                 .orElseThrow(IllegalArgumentException::new);
        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);

        for(MenuProductRequest menuProductRequest : menuRequest.getMenuProductRequests()) {
            Product product = productRepository.findById(menuProductRequest.getProductId())
                                                .orElseThrow(IllegalArgumentException::new);
            menu.addProduct(product, menuProductRequest.getQuantity());
        }
        menu.priceCheck();

        Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
//
//        for (final Menu menu : menus) {
//            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
//        }
        return MenuResponse.ofMenuResponses(menus);
    }
}
