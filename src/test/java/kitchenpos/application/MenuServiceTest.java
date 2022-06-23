package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menuGroup.MenuGroupRepository;
import kitchenpos.domain.menuProduct.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 관련 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuDao menuDao;

    @Mock
    MenuGroupRepository menuGroupRepository;

    @Mock
    ProductRepository productRepository;

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

    @DisplayName("메뉴를 생성할 수 있다")
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

    @DisplayName("메뉴를 생성할 수 있다 - 메뉴의 가격은 0원 이상이어야 한다")
    @Test
    void create_exception1() {
        // given
        Menu request1 = 메뉴_데이터_생성(null, null);

        // when && then
        assertThatThrownBy(() -> 메뉴_생성(request1))
                .isInstanceOf(IllegalArgumentException.class);

        // given
        Menu request2 = 메뉴_데이터_생성(null, BigDecimal.valueOf(-1));

        // when && then
        assertThatThrownBy(() -> 메뉴_생성(request2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 생성할 수 있다 - 유효한 메뉴 그룹이 지정되어야 한다")
    @Test
    void create_exception2() {
        // given
        Menu request = 메뉴_데이터_생성(null, BigDecimal.valueOf(25000));
        given(메뉴_그룹_유효성_확인()).willReturn(false);

        // when && then
        assertThatThrownBy(() -> 메뉴_생성(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 생성할 수 있다 - 유효한 상품들이 지정되어야 한다")
    @Test
    void create_exception3() {
        // given
        Menu request = 메뉴_데이터_생성(null, BigDecimal.valueOf(25000));
        given(메뉴_그룹_유효성_확인()).willReturn(true);
        given(상품_조회(1L)).willReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> 메뉴_생성(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 생성할 수 있다" +
            " - 메뉴의 가격은 포함된 상품들의 금액에 합보다 작거나 같아야 한다")
    @Test
    void create_exception4() {
        // given
        Menu request = 메뉴_데이터_생성(null, BigDecimal.valueOf(26001));
        given(메뉴_그룹_유효성_확인()).willReturn(true);
        given(상품_조회(1L)).willReturn(Optional.of(new Product(1L, "초밥", BigDecimal.valueOf(10000))));
        given(상품_조회(2L)).willReturn(Optional.of(new Product(2L, "우동", BigDecimal.valueOf(3000))));

        // when && then
        assertThatThrownBy(() -> 메뉴_생성(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회할 수 있다")
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
        return productRepository.findById(id);
    }

    private MenuProduct 메뉴_상품_생성(MenuProduct menuProduct) {
        return menuProductDao.save(menuProduct);
    }

    private Menu 메뉴_생성(Menu menu) {
        return menuService.create(menu);
    }

    private boolean 메뉴_그룹_유효성_확인() {
        return menuGroupRepository.existsById(any());
    }
}
