package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuRepository menuRepository;

    public MenuService(MenuDao menuDao, MenuProductDao menuProductDao, MenuGroupRepository menuGroupRepository,
                       ProductRepository productRepository, MenuRepository menuRepository) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        MenuGroup menuGroup = findMenuGroupById(request.getMenuGroupId());
        List<MenuProduct> menuProducts = toMenuProducts(request);
        Menu menu = request.toMenu(menuGroup, menuProducts);
        return MenuResponse.from(menuRepository.save(menu));
    }

    private List<MenuProduct> toMenuProducts(MenuRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(it -> MenuProduct.of(findProductById(it.getProductId()), it.getQuantity()))
                .collect(Collectors.toList());
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    private MenuGroup findMenuGroupById(Long id) {
        return menuGroupRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
