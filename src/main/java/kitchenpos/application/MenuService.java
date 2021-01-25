package kitchenpos.application;

import kitchenpos.advice.exception.MenuException;
import kitchenpos.advice.exception.MenuGroupException;
import kitchenpos.advice.exception.ProductException;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;
    private final MenuGroupService menuGroupService;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository,
            final MenuGroupService menuGroupService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
        this.menuGroupService = menuGroupService;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupService.findMenuGroupById(menuRequest.getMenuGroupId());

        Menu menu = menuRequest.toMenu(menuGroup);

        final BigDecimal price = menu.getPrice();
        validateEmptyPrice(price);
        System.out.println(menu);
        validateExistsMenuGroup(menu);

        final List<MenuProduct> menuProducts = new ArrayList<>();

        final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProducts();
        for (MenuProductRequest request : menuProductRequests) {
            Product product = findProductByProductId(request.getProductId());
            MenuProduct menuProduct = new MenuProduct(menu, product, request.getQuantity());
            menuProducts.add(menuProduct);
        }

        BigDecimal sum = getSumOfPrices(menuProducts);
        validatePriceSum(price, sum);

        return saveMenu(menu, menuProducts);
    }

    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.updateMenuProducts(findMenuProductsByMenu(menu));
        }

        return menus;
    }


    private List<MenuProduct> findMenuProductsByMenu(Menu menu) {
        return menuProductRepository.findAllByMenu(menu);
    }

    private MenuProduct findMenuProductBySeq(Long seq) {
        return menuProductRepository.findById(seq).orElseThrow(()->new MenuException("존재하는 메뉴상품이 없습니다", seq));
    }

    private Product findProductByProductId(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductException("존재하는 상품 id가 없습니다.", id));
    }

    private Menu saveMenu(Menu menu, List<MenuProduct> menuProducts) {
        final Menu savedMenu = menuRepository.save(menu);
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenu(savedMenu);
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }
        savedMenu.updateMenuProducts(savedMenuProducts);
        return savedMenu;
    }


    private BigDecimal getSumOfPrices(List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = menuProduct.getProduct();
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return sum;
    }

    private void validateExistsMenuGroup(Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroup().getId())) {
            throw new MenuGroupException("존재하는 메뉴그룹 id가 없습니다", menu.getMenuGroup().getId());
        }
    }

    private void validateEmptyPrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new MenuException("가격이 0보다 작을 수 없습니다", price.longValue());
        }
    }

    private void validatePriceSum(BigDecimal price, BigDecimal sum) {
        if (price.compareTo(sum) > 0) {
            throw new MenuException("메뉴 가격과 각 가격의 총합보다 큽니다", price.longValue(), sum.longValue());
        }
    }

}
