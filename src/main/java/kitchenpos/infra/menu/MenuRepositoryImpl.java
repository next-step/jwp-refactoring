package kitchenpos.infra.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MenuRepositoryImpl implements MenuRepository {
    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;

    public MenuRepositoryImpl(final MenuDao menuDao, final MenuProductDao menuProductDao) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
    }

    @Override
    public Menu save(final Menu menu) {
        Menu idExistMenu = menuDao.save(menu);
        List<MenuProduct> menuProducts = menu.getMenuProducts().stream()
                .map(it -> menuProductDao.save(MenuProduct.of(idExistMenu.getId(), it.getProductId(), it.getQuantity())))
                .collect(Collectors.toList());
        return new Menu(
                idExistMenu.getId(),
                idExistMenu.getName(),
                idExistMenu.getPrice(),
                idExistMenu.getMenuGroupId(),
                menuProducts
        );
    }

    @Override
    public List<Menu> findAll() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(menu.getId());
            menuProducts.forEach(menu::addMenuProduct);
        }

        return menus;
    }
}
