package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
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
        final Menu addMenu = menuRequest.toMenu(menuGroup);
        final Menu savedMenu = menuRepository.save(addMenu);
        final List<MenuProduct> menuProducts = menuRequest.getMenuProducts();
        savedMenu.addMenuProducts(menuProducts);
        final BigDecimal price = menuRequest.getPrice();
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
        
        /*for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenu(savedMenu);
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }*/

        return savedMenu;
    }

    private MenuGroup getMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
