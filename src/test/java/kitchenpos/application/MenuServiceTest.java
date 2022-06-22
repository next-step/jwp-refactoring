package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 관련 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuDao menuDao;

    @Mock
    MenuGroupDao menuGroupDao;

    @Mock
    ProductDao productDao;

    @Mock
    MenuProductDao menuProductDao;

    @InjectMocks
    MenuService menuService;

    private MenuProduct menuProductSeq1;
    private MenuProduct menuProductSeq2;
    private List<MenuProduct> menuProducts;

    @BeforeEach
    void setUp() {
        menuProductSeq1 = new MenuProduct(1L, 1L, 1L, 2);
        menuProductSeq2 = new MenuProduct(2L, 1L, 2L, 2);
        menuProducts = Arrays.asList(menuProductSeq1, menuProductSeq2);
    }

    @DisplayName("메뉴 생성")
    @Test
    void create() {
        // given
        Menu request = 메뉴_데이터_생성(null, BigDecimal.valueOf(26000));
        Menu 예상값 = 메뉴_데이터_생성(1L, BigDecimal.valueOf(26000));
        given(메뉴_그룹_유효성_확인()).willReturn(true);
        given(상품_조회(1L)).willReturn(Optional.of(new Product(1L, "초밥", BigDecimal.valueOf(10000))));
        given(상품_조회(2L)).willReturn(Optional.of(new Product(2L, "우동", BigDecimal.valueOf(3000))));
        given(메뉴_상품_생성(menuProductSeq1)).willReturn(menuProductSeq1);
        given(메뉴_상품_생성(menuProductSeq2)).willReturn(menuProductSeq2);
        given(menuDao.save(request)).willReturn(예상값);

        // when
        Menu 메뉴_생성_결과 = 메뉴_생성(request);

        // then
        메뉴_값_비교(예상값, 메뉴_생성_결과);
    }

    @DisplayName("메뉴 생성 - price가 값이 없는 경우")
    @Test
    void create_exception1() {
        // given
        Menu request = 메뉴_데이터_생성(null, null);

        // when && then
        assertThatThrownBy(() -> 메뉴_생성(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 - price 값이 0 미만일 경우")
    @Test
    void create_exception2() {
        // given
        Menu request = 메뉴_데이터_생성(null, BigDecimal.valueOf(-1));

        // when && then
        assertThatThrownBy(() -> 메뉴_생성(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 - 메뉴 그룹이 유효하지 않은 경우")
    @Test
    void create_exception3() {
        // given
        Menu request = 메뉴_데이터_생성(null, BigDecimal.valueOf(25000));
        given(메뉴_그룹_유효성_확인()).willReturn(false);

        // when && then
        assertThatThrownBy(() -> 메뉴_생성(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 - 상품이 유효하지 않은 경우")
    @Test
    void create_exception4() {
        // given
        Menu request = 메뉴_데이터_생성(null, BigDecimal.valueOf(25000));
        given(메뉴_그룹_유효성_확인()).willReturn(true);
        given(상품_조회(1L)).willReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> 메뉴_생성(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 생성 - 메뉴의 가격이 포함된 상품들의 가격(수량*가격)보다 클 경우")
    @Test
    void create_exception5() {
        // given
        Menu request = 메뉴_데이터_생성(null, BigDecimal.valueOf(26001));
        given(메뉴_그룹_유효성_확인()).willReturn(true);
        given(상품_조회(1L)).willReturn(Optional.of(new Product(1L, "초밥", BigDecimal.valueOf(10000))));
        given(상품_조회(2L)).willReturn(Optional.of(new Product(2L, "우동", BigDecimal.valueOf(3000))));

        // when && then
        assertThatThrownBy(() -> 메뉴_생성(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    void list() {
        // given
        List<Menu> 예상값 = Arrays.asList(
                메뉴_데이터_생성(1L, BigDecimal.valueOf(10000)),
                메뉴_데이터_생성(2L, BigDecimal.valueOf(20000))
        );
        given(menuDao.findAll()).willReturn(예상값);

        // when
        List<Menu> 메뉴_목록_조회_결과 = menuService.list();

        // then
        assertAll(
                () -> 메뉴_값_비교(예상값.get(0), 메뉴_목록_조회_결과.get(0)),
                () -> 메뉴_값_비교(예상값.get(1), 메뉴_목록_조회_결과.get(1))
        );
    }

    private Menu 메뉴_데이터_생성(Long id, BigDecimal price) {
        return new Menu(id, "메뉴이름", price, 1L, menuProducts);
    }

    private void 메뉴_값_비교(Menu result, Menu expectation) {
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(expectation.getId()),
                () -> assertThat(result.getName()).isEqualTo(expectation.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(expectation.getPrice()),
                () -> assertThat(result.getMenuGroupId()).isEqualTo(expectation.getMenuGroupId()),
                () -> assertThat(result.getMenuProducts()).isEqualTo(expectation.getMenuProducts())
        );
    }

    private Optional<Product> 상품_조회(long id) {
        return productDao.findById(id);
    }

    private MenuProduct 메뉴_상품_생성(MenuProduct menuProduct) {
        return menuProductDao.save(menuProduct);
    }

    private Menu 메뉴_생성(Menu menu) {
        return menuService.create(menu);
    }

    private boolean 메뉴_그룹_유효성_확인() {
        return menuGroupDao.existsById(any());
    }
}
