package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.domain.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductDao productDao;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductDao productDao
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = getMenuGroup(menuRequest.getMenuGroupId());
        final Menu menu = menuRequest.toMenu(menuGroup);
        final List<MenuProduct> menuProducts = getMenuProducts(menuRequest.getMenuProducts());

        menu.addMenuProducts(menuProducts);

        final Menu savedMenu = menuRepository.save(menu);

        return savedMenu;
    }

    private List<MenuProduct> getMenuProducts(List<MenuProductRequest> menuProducts) {
        List<MenuProduct> result = new ArrayList<>();
        for (final MenuProductRequest menuProductRequest : menuProducts) {
            final Product product = productDao.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            result.add(new MenuProduct(product, menuProductRequest.getQuantity()));
        }
        return result;
    }

    private MenuGroup getMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
