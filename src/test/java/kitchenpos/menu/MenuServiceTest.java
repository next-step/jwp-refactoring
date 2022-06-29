package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.MenuService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    MenuService menuService;

    @Mock
    MenuDao menuDao;

    @Mock
    MenuGroupDao menuGroupDao;

    @Mock
    MenuProductDao menuProductDao;

    @Mock
    ProductDao productDao;

    Menu 후라이드치킨 = new Menu();
    Product 후라이드 = new Product();
    MenuProduct 후라이드치킨상품 = new MenuProduct();

    @BeforeEach
    void setUp() {
        createProduct();
        createMenu();
        createMenuProduct();
    }

    void createProduct() {
        후라이드.setId(1L);
        후라이드.setName("후라이드");
        후라이드.setPrice(new BigDecimal(15000));
    }

    void createMenu() {
        후라이드치킨.setId(1L);
        후라이드치킨.setName("후라이드치킨");
        후라이드치킨.setPrice(new BigDecimal(15000));
    }

    void createMenuProduct() {
        후라이드치킨상품.setMenuId(후라이드치킨.getId());
        후라이드치킨상품.setProductId(후라이드.getId());
        후라이드치킨상품.setQuantity(1L);
        후라이드치킨.setMenuProducts(Collections.singletonList(후라이드치킨상품));
    }

    @Test
    @DisplayName("메뉴를 저장한다")
    void create() {
        // given
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(any())).willReturn(Optional.ofNullable(후라이드));
        given(menuDao.save(any())).willReturn(후라이드치킨);
        given(menuProductDao.save(any())).willReturn(후라이드치킨상품);

        // when
        Menu actual = menuService.create(후라이드치킨);

        // then
        assertThat(actual).isEqualTo(후라이드치킨);
    }

    @Test
    @DisplayName("메뉴 저장시 메뉴의 금액은 0원 이상이다")
    void create_priceException() {
        // given
        Menu 양념치킨 = new Menu();
        양념치킨.setPrice(new BigDecimal(-1));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuService.create(양념치킨)
        );
    }

    @Test
    @DisplayName("메뉴 저장시 메뉴는 존재하는 메뉴그룹 정보를 가지고 있다")
    void create_nonExistMenuGroupError() {
        // given
        given(menuGroupDao.existsById(후라이드치킨.getMenuGroupId())).willReturn(false);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuService.create(후라이드치킨)
        );
    }

    @Test
    @DisplayName("메뉴 저장시 메뉴는 존재하는 상품 정보를 가져야 한다")
    void create_nonProductInfoError() {
        // given
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(후라이드치킨상품.getProductId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuService.create(후라이드치킨)
        );
    }

    @Test
    @DisplayName("메뉴 저장시 메뉴상품에 속한 상품들의 금액 합보다 메뉴 가격이 작아야 한다")
    void create_totalPriceError() {
        // given
        후라이드치킨.setPrice(new BigDecimal(20000));
        given(menuGroupDao.existsById(any())).willReturn(true);
        given(productDao.findById(후라이드치킨상품.getProductId())).willReturn(Optional.ofNullable(후라이드));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuService.create(후라이드치킨)
        );
    }

    @Test
    @DisplayName("메뉴 리스트를 조회한다")
    void list() {
        // given
        given(menuDao.findAll()).willReturn(Collections.singletonList(후라이드치킨));
        given(menuProductDao.findAllByMenuId(후라이드치킨.getId())).willReturn(Collections.singletonList(후라이드치킨상품));

        List<Menu> actual = menuService.list();

        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual).containsExactly(후라이드치킨)
        );
    }
}
