package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public Menu create(final Menu menu) {
        List<MenuProductCreate> collect = menu.getMenuProducts()
                .stream()
                .map(item -> new MenuProductCreate(item.getMenuId(), item.getProductId(), item.getQuantity()))
                .collect(Collectors.toList());

        MenuCreate menuCreate = new MenuCreate(menu.getName(), menu.getPrice(), menu.getMenuGroupId(), collect);
        return create(menuCreate);
    }

    @Transactional
    public Menu create(final MenuCreate create) {
        MenuGroup menuGroup = menuGroupDao.findById(create.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        Products products = new Products(productDao.findAllById(create.getProductsIdInMenuProducts()));

        return menuDao.save(Menu.create(create, menuGroup, products));
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }
}
