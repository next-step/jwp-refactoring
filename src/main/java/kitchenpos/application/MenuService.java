package kitchenpos.application;

import java.security.InvalidParameterException;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuService(
        final MenuDao menuDao,
        final MenuGroupDao menuGroupDao,
        final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = findMenuGroup(menuRequest.getMenuGroupId());
        List<MenuProduct> menuProducts = getMenuProducts(menuRequest);

        Menu menu = menuRequest.toMenu(menuGroup, menuProducts);

        return MenuResponse.of(menuDao.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return MenuResponse.toList(menuDao.findAll());
    }

    @Transactional(readOnly = true)
    public MenuGroup findMenuGroup(Long menuGroupId) {
        return menuGroupDao.findById(menuGroupId)
            .orElseThrow(() -> new InvalidParameterException("없는 메뉴그룹입니다."));
    }

    @Transactional(readOnly = true)
    public List<MenuProduct> getMenuProducts(MenuRequest menuRequest) {
        List<Long> productIds = menuRequest.getProductIds();
        List<Product> products = productDao.findAllById(productIds);

        if (products.size() != productIds.size()) {
            throw new InvalidParameterException("존재하지 않는 상품이 있습니다.");
        }

        return products.stream()
            .map(
                product -> MenuProduct.of(product, menuRequest.getProductQuantity(product.getId())))
            .collect(Collectors.toList());
    }
}
