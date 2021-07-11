package kitchenposNew.menu.application;

import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.MenuProduct;
import kitchenposNew.menu.domain.Product;
import kitchenposNew.menu.domain.Menu;
import kitchenposNew.menu.domain.MenuGroupRepository;
import kitchenposNew.menu.domain.MenuRepository;
import kitchenposNew.menu.domain.ProductRepository;
import kitchenposNew.menu.dto.MenuRequest;
import kitchenposNew.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductDao menuProductDao;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository, MenuProductDao menuProductDao, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductDao = menuProductDao;
        this.productRepository = productRepository;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElseThrow(IllegalArgumentException::new);
        List<Product> products = menuRequest.getProductIds().stream()
                .map(productId -> productRepository.findById(productId).orElseThrow(IllegalArgumentException::new))
                .collect(Collectors.toList());
        Menu persistMenu = menuRepository.save(menuRequest.toMenu(products));
        return MenuResponse.of(persistMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(menu -> MenuResponse.of(menu))
                .collect(Collectors.toList());
    }
}
