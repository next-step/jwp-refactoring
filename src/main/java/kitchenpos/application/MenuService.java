package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.domain.MenuProducts;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuResponses;
import kitchenpos.exception.KitchenposNotFoundException;

@Service
@Transactional(readOnly = true)
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
        MenuGroup menuGroup = menuGroupDao.findById(menuRequest.getMenuGroupId())
            .orElseThrow(KitchenposNotFoundException::new);

        MenuProducts menuProducts = makeMenuProducts(menuRequest.getMenuProducts());
        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuProducts);

        final Menu savedMenu = menuDao.save(menu);

        return MenuResponse.from(savedMenu);
    }

    private MenuProducts makeMenuProducts(List<MenuProductRequest> menuProductRequests) {
        return new MenuProducts(menuProductRequests.stream()
            .map(menuProductRequest -> {
                Product product = productDao.findById(menuProductRequest.getProductId())
                    .orElseThrow(KitchenposNotFoundException::new);
                return new MenuProduct(product, menuProductRequest.getQuantity());
            })
            .collect(Collectors.toList()));
    }

    public MenuResponses list() {
        final List<Menu> menus = menuDao.findAll();
        return MenuResponses.from(menus);
    }
}
