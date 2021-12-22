package kitchenpos.menu.application;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import kitchenpos.common.exception.NoResultDataException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupDao;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Amount;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        final MenuGroup menuGroup = menuGroupDao.findById(menuRequest.getMenuGroupId())
            .orElseThrow(NoResultDataException::new);

        final Menu menu = Menu.of(
            menuRequest.getName(),
            Amount.of(menuRequest.getPrice()),
            menuGroup
        );

        menu.withMenuProducts(createMenuProducts(menuRequest, menu));
        return MenuResponse.of(menuDao.save(menu));
    }

    public List<MenuResponse> list() {
        return MenuResponse.ofList(menuDao.findAll());
    }

    private List<MenuProduct> createMenuProducts(final MenuRequest menuRequest, final Menu menu) {
        return menuRequest.getMenuProducts()
            .stream()
            .map(findByProductIdToMenuProduct(menu))
            .collect(Collectors.toList());
    }

    private Function<MenuProductRequest, MenuProduct> findByProductIdToMenuProduct(final Menu menu) {
        return menuProduct -> {
            Product product = productDao.findById(menuProduct.getProductId())
                .orElseThrow(NoResultDataException::new);
            return MenuProduct.of(menu, product, menuProduct.getQuantity());
        };
    }

}
