package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuDao menuDao;
    private final ProductDao productDao;

    public MenuService(MenuDao menuDao, ProductDao productDao) {
        this.menuDao = menuDao;
        this.productDao = productDao;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        List<MenuProduct> menuProducts = makeMenuProducts(menuRequest.getMenuProducts());
        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(), menuProducts);
        return MenuResponse.of(menuDao.save(menu));
    }

    private List<MenuProduct> makeMenuProducts(List<MenuProductRequest> menuProductRequests) {
        List<Long> productIds = findProductIds(menuProductRequests);
        return replaceWithSavedProduct(menuProductRequests, productDao.findAllById(productIds));
    }

    private List<Long> findProductIds(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
            .map(MenuProductRequest::getProductId)
            .collect(Collectors.toList());
    }

    private List<MenuProduct> replaceWithSavedProduct(List<MenuProductRequest> menuProductRequests, List<Product> products) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = findProductById(products, menuProductRequest.getProductId());
            menuProducts.add(new MenuProduct(product, menuProductRequest.getQuantity()));
        }
        return menuProducts;
    }

    private Product findProductById(List<Product> products, Long productId) {
        return products.stream()
            .filter(product -> product.getId().equals(productId))
            .findFirst()
            .orElseThrow(EntityNotFoundException::new);
    }

    public List<MenuResponse> list() {
        return MenuResponse.listOf(menuDao.findAll());
    }
}
