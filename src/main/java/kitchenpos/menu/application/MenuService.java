package kitchenpos.menu.application;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import kitchenpos.common.exception.NoResultDataException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Amount;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;


    public MenuService(
        final MenuDao menuDao,
        final MenuGroupService menuGroupService,
        final ProductService productService
    ) {
        this.menuDao = menuDao;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {

        final Menu menu = Menu.of(
            menuRequest.getName(),
            Amount.of(menuRequest.getPrice()),
            menuGroupService.findByIdThrow(menuRequest.getMenuGroupId())
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
        return menuProduct -> MenuProduct.of(
            menu,
            productService.findByIdThrow(menuProduct.getProductId()),
            menuProduct.getQuantity()
        );
    }

    public Long findMenuNoById(Long id) {
        Menu menu = menuDao.findById(id)
            .orElseThrow(NoResultDataException::new);
        return menu.getId();
    }

}
