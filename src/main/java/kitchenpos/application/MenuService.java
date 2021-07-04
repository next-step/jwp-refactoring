package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final ProductDao productDao;
    private final MenuGroupService menuGroupService;

    public MenuService(
            final MenuDao menuDao,

            ProductDao productDao,
            final MenuGroupService menuGroupService
    ) {
        this.menuDao = menuDao;
        this.productDao = productDao;
        this.menuGroupService = menuGroupService;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {

        MenuGroup findMenuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());

        if (menuGroupService.isExists(findMenuGroup)) {
            throw new IllegalArgumentException("existed menuGroup");
        }

        Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), findMenuGroup, new ArrayList<>());

        List<MenuProductRequest> menuProducts = menuRequest.getMenuProducts();

        menuProducts.stream().map( menuProduct ->
                MenuProduct.of(null,
                        productDao.findById(menuProduct.getProductId()).orElseThrow(IllegalArgumentException::new),
                        menuProduct.getQuantity()
        )).forEach(menu::addMenuProducts);

        if (menu.isReasonablePrice() == false) {
            throw new IllegalArgumentException("Total Price is higher then expected MenuProduct Price");
        }

        return menuDao.save(menu);
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }
}
