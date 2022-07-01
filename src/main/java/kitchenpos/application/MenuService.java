package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
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
    public Menu create(final Menu menu) {
        final BigDecimal price = menu.getPrice();

        // TODO : Menu Entity 객체 생성 시점에 원시 타입 포장 객체를 이용한 유효성 검증
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        // TODO : 오류 메세지 추가
        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        // TODO : productDao.findByAllIds 를 이용하여 In절로 조회 -> 조회 쿼리 수 감소
        // TODO : findBByAllIds와 menuProducts size를 비교하는 구문으로 유효성 검증 변경
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            // TODO : Menu 도메인 내부에서 처리하도록 로직 이동 -> e.g. menu.calculateSum()
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        // TODO : Menu 도메인 내부의 sum 계산 메소드에서 유효성 검증을 완료한 후 반환 하도록 수정
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        // TODO : Menu Entity와 MenuProduct Entity의 생명 주기가 같도록 영속성 전이 옵션 설정
        // TODO : e.g. @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        // TODO N+1 발생
        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
