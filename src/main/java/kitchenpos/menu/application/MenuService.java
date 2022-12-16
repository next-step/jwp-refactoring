package kitchenpos.menu.application;

import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MenuService {
    public static final String PRICE_NOT_NULL_EXCEPTION_MESSAGE = "가격은 필수입니다.";
    public static final String MENU_GROUP_NOT_EXIST_EXCEPTION_MESSAGE = "메뉴 그룹이 존재하지 않습니다.";
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {

        if (request.getMenuGroupId() == null) {
            throw new IllegalArgumentException(MENU_GROUP_NOT_EXIST_EXCEPTION_MESSAGE);
        }

        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException(MENU_GROUP_NOT_EXIST_EXCEPTION_MESSAGE);
        }

        final List<MenuProduct> menuProducts = request.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            productRepository.findById(menuProduct.getProduct().getId())
                    .orElseThrow(IllegalArgumentException::new);
        }
        Menu menu = new Menu(new Name(request.getName()), new Price(request.getPrice()), request.getMenuGroupId(), menuProducts);
//
//        final Long menuId = savedMenu.getId();
//        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
//        for (final MenuProduct menuProduct : menuProducts) {
//            menuProduct.setMenuId(menuId);
//            savedMenuProducts.add(menuProductDao.save(menuProduct));
//        }
//        savedMenu.setMenuProducts(savedMenuProducts);

        return new MenuResponse(menuRepository.save(menu));
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}

