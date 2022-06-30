package kitchenpos.application;

import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuProductCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {
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
    public Menu create(final MenuCreateRequest request) {
        final BigDecimal price = request.getPrice();

        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }

        final List<MenuProductCreateRequest> menuProducts = request.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductCreateRequest menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 메뉴 상품 가격의 총합보다 높을 수 없습니다.");
        }

        final Menu savedMenu = menuRepository.save(request.toEntity());

        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProductCreateRequest menuProductCreateRequest : menuProducts) {
            MenuProduct menuProduct = menuProductCreateRequest.toEntity();
            menuProduct.updateMenu(savedMenu);
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }
        savedMenu.changeMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.changeMenuProducts(menuProductRepository.findAllByMenu(menu));
        }

        return menus;
    }
}
