package kitchenpos.menu.application;

import kitchenpos.common.domian.Price;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.common.error.ErrorInfo;
import kitchenpos.common.error.CustomException;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.repository.MenuDao;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupDao;
import kitchenpos.menuproduct.dto.MenuProductRequest;
import kitchenpos.menuproduct.repository.MenuProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductDao;
import kitchenpos.menu.domain.Menu;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        MenuGroup menuGroup = menuGroupDao.findById(menuRequest.getMenuGroupId()).orElseThrow(() -> new CustomException(ErrorInfo.NOT_FOUND_MENU_GROUP));
        List<MenuProductRequest> menuProductsRequest = menuRequest.getMenuProducts();

        Price totalPrice = new Price();
        for (MenuProductRequest menuProduct : menuProductsRequest) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(() -> new CustomException(ErrorInfo.NOT_FOUND_PRODUCT));
            totalPrice = totalPrice.sum(product.getPrice(), menuProduct.getQuantity());
        }

        if (!menuRequest.getPrice().equals(totalPrice)) {
            throw new CustomException(ErrorInfo.TOTAL_PRICE_NOT_EQUAL_REQUEST);
        }

        MenuProducts menuProducts = new MenuProducts();
        return menuDao.save(Menu.of(menuGroup, menuRequest.getName(), menuRequest.getPrice(), menuProducts)).toResponse();
    }

    public List<MenuResponse> list() {
        return menuDao.findAll().stream().map(Menu::toResponse).collect(Collectors.toList());
    }
}
