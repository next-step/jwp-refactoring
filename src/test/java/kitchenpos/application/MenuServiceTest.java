package kitchenpos.application;

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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 기능 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    private Menu menu;
    private MenuProduct firstMenuProduct;
    private MenuProduct secondMenuProduct;
    private Product firstProduct;
    private Product secondProduct;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private MenuService menuService;

    @BeforeEach
    public void setUp() {
        menu = new Menu();
    }

    @Test
    @DisplayName("메뉴 등록 금액 예외 테스트")
    void 메뉴_등록_금액_예외_테스트() {
        // given
        // 잘못 된 금액이 적용 되어 있음
        menu.setPrice(BigDecimal.valueOf(-12));

        // then
        // 등록 요청 시 예외 발생
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재 하지 않는 메뉴 그룹 ID에 등록 시 예외")
    void isNotExistMenuGroup_exception() {
        // given
        // 존재 하지 않는 메뉴 그룹 ID가 등록 되어 있음
        menu.setPrice(BigDecimal.valueOf(1000));
        menu.setMenuGroupId(1L);

        // then
        // 등록 요청 시 예외 발생
        when(menuGroupDao.existsById(any())).thenReturn(false);
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 상품 리스트에 존재하지 않는 상품이 있음")
    void isNotExistProduct_exception() {
        // given
        // 등록되지 않은 상품이 등록되어 있음
        메뉴_그룹_등록_되어_있음();

        // and
        // 메뉴에 등록되어 있음
        menu.setPrice(BigDecimal.valueOf(1000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Arrays.asList(firstMenuProduct));

        // and
        // 메뉴 그릅 등록되어 있음
        when(menuGroupDao.existsById(any())).thenReturn(true);

        // then
        // 등록 요청 시 예외 발생
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 총 가격은 상품들의 가격의 합과 같거나 작아야 함")
    void isExpensiveMenuPriceSumThanProductAllPrice_exception() {
        // given
        // 메뉴에 상픔이 등록되어 있음
        메뉴_그룹_등록_되어_있음();
        상품_등록_되어_있음();

        // and
        // 메뉴에 등록되어 있음
        menu.setPrice(BigDecimal.valueOf(2100));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Arrays.asList(firstMenuProduct, secondMenuProduct));

        // and
        // 메뉴 그릅과 상품이 등록되어 있음
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(1L)).thenReturn(Optional.ofNullable(firstProduct));
        when(productDao.findById(2L)).thenReturn(Optional.ofNullable(secondProduct));

        // then
        // 등록 요청 시 예외 발생
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴_정상_등록_테스트")
    void 메뉴_정상_등록_테스트() {
        // given
        // 메뉴에 상픔이 등록되어 있음
        메뉴_그룹_등록_되어_있음();
        상품_등록_되어_있음();

        // and
        // 메뉴에 등록되어 있음
        menu.setPrice(BigDecimal.valueOf(2000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Arrays.asList(firstMenuProduct, secondMenuProduct));

        // and
        // 메뉴 그릅과 상품이 등록되어 있음
        when(menuGroupDao.existsById(any())).thenReturn(true);
        when(productDao.findById(1L)).thenReturn(Optional.ofNullable(firstProduct));
        when(productDao.findById(2L)).thenReturn(Optional.ofNullable(secondProduct));

        // and
        // 메뉴와 메뉴에 등록된 상품들이 등록되어 있음
        when(menuDao.save(menu)).thenReturn(menu);
        when(menuProductDao.save(firstMenuProduct)).thenReturn(firstMenuProduct);
        when(menuProductDao.save(secondMenuProduct)).thenReturn(secondMenuProduct);

        // then
        // 정상 등록 됨
        Menu expected = menuService.create(menu);
        assertThat(expected).isNotNull();
        assertThat(expected.getMenuProducts().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("메뉴_정상_조회_테스트")
    void 메뉴_정상_조회_테스트() {
        // given
        // 메뉴에 상픔이 등록되어 있음
        메뉴_그룹_등록_되어_있음();

        // and
        // 메뉴에 등록되어 있음
        when(menuDao.findAll()).thenReturn(Arrays.asList(menu));
        when(menuProductDao.findAllByMenuId(any())).thenReturn(Arrays.asList(firstMenuProduct, secondMenuProduct));

        // then
        // 정상 조회 됨
        List<Menu> expected = menuService.list();
        assertThat(expected.size()).isNotZero();
        assertThat(expected.get(0).getMenuProducts().size()).isNotZero();
    }

    private void 상품_등록_되어_있음() {
        firstProduct = new Product();
        firstProduct.setId(1L);
        firstProduct.setPrice(BigDecimal.valueOf(1000));
        secondProduct = new Product();
        secondProduct.setId(2L);
        secondProduct.setPrice(BigDecimal.valueOf(1000));
    }

    private void 메뉴_그룹_등록_되어_있음() {
        firstMenuProduct = new MenuProduct();
        firstMenuProduct.setProductId(1L);
        firstMenuProduct.setQuantity(1);
        secondMenuProduct = new MenuProduct();
        secondMenuProduct.setProductId(2L);
        secondMenuProduct.setQuantity(1);
    }


}
