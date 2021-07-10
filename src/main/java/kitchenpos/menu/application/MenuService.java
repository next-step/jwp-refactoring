package kitchenpos.menu.application;

import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menugroup.domain.MenuGroupEntity;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    private final MenuGroupService menuGroupService;
    private final ProductService productService;
    private final MenuRepository menuRepository;

    public MenuService(MenuDao menuDao, MenuGroupDao menuGroupDao, MenuProductDao menuProductDao, ProductDao productDao, MenuGroupService menuGroupService, ProductService productService, MenuRepository menuRepository) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public Menu create(final Menu menu) {
        final BigDecimal price = menu.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴 가격은 0원 이상이어야 합니다.");
        }

        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴그룹입니다.");
        }

        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 상품입니다."));
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴 가격은 상품 가격의 총 합보다 클 수 없습니다.");
        }

        final Menu savedMenu = menuDao.save(menu);

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    @Transactional
    public MenuResponse createTemp(MenuRequest menuRequest) {
        priceValidCheck(menuRequest.getPrice());
        MenuGroupEntity menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());
        MenuProducts menuProducts = findMenuProducts(menuRequest.getMenuProducts());
        MenuEntity menuEntity = new MenuEntity(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuProducts);

        return MenuResponse.of(menuRepository.save(menuEntity));
    }

    private void priceValidCheck(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴 가격은 0원 이상이어야 합니다.");
        }
    }

    private MenuProducts findMenuProducts(List<MenuProductRequest> menuProductRequests) {
        productValidCheck(menuProductRequests);
        List<MenuProductEntity> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            ProductEntity productEntity = productService.findById(menuProductRequest.getProductId());
            menuProducts.add(menuProductRequest.toEntity(productEntity));
        }
        return new MenuProducts(menuProducts);
    }

    private void productValidCheck(List<MenuProductRequest> menuProductRequests) {
        if (menuProductRequests == null || menuProductRequests.isEmpty()) {
            throw new IllegalArgumentException("포함된 상품이 없습니다.");
        }
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();
        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }
        return menus;
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> listTemp() {
        return MenuResponse.ofList(menuRepository.findAll());
    }
}
