package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
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

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao,
            final MenuProductDao menuProductDao,
            final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }


    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        // 1. 가격이 빈 값 인지 체크
        final BigDecimal price = menuRequest.getPrice();
        if(price == null || price.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException();
        }

        // 2. 메뉴그룹 정보가 존재하는 정보인지 체크
        if ( menuRequest.getMenuGroupId() == null || !menuGroupDao.existsById(menuRequest.getMenuGroupId()) ) {
            throw new IllegalArgumentException();
        }

        // 3. 구성 상품의 가격의 합 > 메뉴의 가격 인지확인
        final List<MenuProductRequest> menuProducts = menuRequest.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        // 메뉴 상품 저장
        MenuGroup menuGroup = menuGroupDao.findById(menuRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        Menu menu = MenuRequest.toMenu(menuRequest, menuGroup);
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProductRequest menuProductRequest : menuProducts) {
            final Product product = productDao.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            savedMenuProducts.add(new MenuProduct(null, menu, product, menuProductRequest.getQuantity()));
        }
        menu.setMenuProducts(savedMenuProducts);

        // 메뉴 저장
        return menuDao.save(menu);
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }

    public List<Menu> list2() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
