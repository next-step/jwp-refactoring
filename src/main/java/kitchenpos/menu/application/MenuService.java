package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.common.domian.Price;
import kitchenpos.common.domian.Quantity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.ProductsQuantities;
import kitchenpos.menu.domain.Quantities;
import kitchenpos.menu.domain.Products;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.common.error.ErrorInfo;
import kitchenpos.common.error.CustomException;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.repository.MenuDao;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupDao;
import kitchenpos.menuproduct.dto.MenuProductRequest;
import kitchenpos.menuproduct.repository.MenuProductDao;
import kitchenpos.product.repository.ProductDao;
import kitchenpos.menu.domain.Menu;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao,
            final MenuProductDao menuProductDao,
            final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupDao.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new CustomException(ErrorInfo.NOT_FOUND_MENU_GROUP));

        List<MenuProductRequest> menuProductsRequest = menuRequest.getMenuProducts();

        ProductsQuantities productsQuantities = new ProductsQuantities(
                new Products(productDao.findByIds(menuProductsRequest.stream()
                        .map(MenuProductRequest::getProductId)
                        .collect(Collectors.toList()))
                        , menuProductsRequest.size())
                ,
                new Quantities(menuProductsRequest.stream()
                        .collect(Collectors.toMap(MenuProductRequest::getProductId, request -> new Quantity(request.getQuantity())))
                        , menuProductsRequest.size())
                ,
                new Price(menuRequest.getPrice())
        );

        return menuDao.save(Menu.of(menuGroup, menuRequest.getName(), productsQuantities)).toResponse();
    }

    public List<MenuResponse> list() {
        return menuDao.findAll().stream().map(Menu::toResponse).collect(Collectors.toList());
    }
}
