package kitchenpos.menu.application;

import kitchenpos.menugroup.fixture.MenuGroupTestFixture;
import kitchenpos.product.fixture.ProductTestFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.validator.MenuValidator;
import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;
import static kitchenpos.menu.fixture.MenuProductTestFixture.*;
import static kitchenpos.menu.fixture.MenuTestFixture.메뉴세트;
import static kitchenpos.menu.fixture.MenuTestFixture.메뉴세트요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuValidator menuValidator;

    @InjectMocks
    private MenuService menuService;

    private MenuGroup 중국집1인메뉴세트그룹;
    private MenuProductRequest 짜장면메뉴상품요청;
    private MenuProductRequest 짬뽕메뉴상품요청;
    private MenuProductRequest 탕수육메뉴상품요청;
    private MenuProductRequest 단무지메뉴상품요청;
    private MenuRequest 짜장면_탕수육_1인_메뉴_세트_요청;
    private MenuRequest 짬뽕_탕수육_1인_메뉴_세트_요청;
    private Menu 짜장면_탕수육_1인_메뉴_세트;

    @BeforeEach
    public void setUp() {
        중국집1인메뉴세트그룹 = MenuGroupTestFixture.중국집1인메뉴세트그룹(MenuGroupTestFixture.중국집1인메뉴세트그룹요청());
        짜장면메뉴상품요청 = 짜장면메뉴상품요청(1L);
        탕수육메뉴상품요청 = 탕수육메뉴상품요청(2L);
        짬뽕메뉴상품요청 = 짬뽕메뉴상품요청(3L);
        단무지메뉴상품요청 = 단무지메뉴상품요청(4L);
        짜장면_탕수육_1인_메뉴_세트_요청 = 메뉴세트요청("짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(20000L),
                중국집1인메뉴세트그룹.getId(), Arrays.asList(짜장면메뉴상품요청, 탕수육메뉴상품요청, 단무지메뉴상품요청));
        짬뽕_탕수육_1인_메뉴_세트_요청 = 메뉴세트요청("짬뽕_탕수육_1인_메뉴_세트", BigDecimal.valueOf(21000L),
                중국집1인메뉴세트그룹.getId(), Arrays.asList(짬뽕메뉴상품요청, 탕수육메뉴상품요청, 단무지메뉴상품요청));
        짜장면_탕수육_1인_메뉴_세트 = 메뉴세트(짜장면_탕수육_1인_메뉴_세트_요청, 1L);
    }

    @DisplayName("메뉴 생성 작업을 성공한다.")
    @Test
    void create() {
        // given
        when(menuRepository.save(any())).thenReturn(짜장면_탕수육_1인_메뉴_세트);
        Product 짜장면상품 = ProductTestFixture.상품생성(ProductTestFixture.짜장면요청());
        ProductTestFixture.setId(1L, 짜장면상품);
        Product 탕수육상품 = ProductTestFixture.상품생성(ProductTestFixture.탕수육요청());
        ProductTestFixture.setId(2L, 탕수육상품);
        Product 단무지상품 = ProductTestFixture.상품생성(ProductTestFixture.단무지요청());
        ProductTestFixture.setId(4L, 단무지상품);
        when(menuValidator.findAllByIdIn(any())).thenReturn(Arrays.asList(짜장면상품, 탕수육상품, 단무지상품));

        // when
        MenuResponse saveMenu = menuService.create(짜장면_탕수육_1인_메뉴_세트_요청);

        // then
        assertAll(
                () -> assertThat(saveMenu).isNotNull()
        );

    }

    @DisplayName("가격이 0원 미만인 메뉴를 생성하면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException() {
        // given
        MenuRequest menu = 메뉴세트요청("짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(-1000L),
                짜장면_탕수육_1인_메뉴_세트.getId(), Arrays.asList(짜장면메뉴상품요청, 탕수육메뉴상품요청, 단무지메뉴상품요청));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("존재하지 않는 메뉴그룹이 포함된 메뉴를 생성하면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException2() {
        // given
        MenuRequest menu = 메뉴세트요청("짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(20000L), 10L,
                Arrays.asList(짜장면메뉴상품요청, 탕수육메뉴상품요청, 단무지메뉴상품요청));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("존재하지 않는 상품이 포함된 메뉴를 생성하면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException3() {
        // given
        MenuRequest menu = 메뉴세트요청("짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(20000L), 중국집1인메뉴세트그룹.getId(),
                singletonList(짜장면메뉴상품요청));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴의 가격이 메뉴 상품들의 가격의 합보다 큰 메뉴를 생성하면 IllegalArgumentException을 반환한다.")
    @Test
    void createWithException4() {
        // given
        MenuRequest menu = 메뉴세트요청("짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(21000L), 중국집1인메뉴세트그룹.getId(),
                Arrays.asList(짜장면메뉴상품요청, 탕수육메뉴상품요청, 단무지메뉴상품요청));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴 전체 목록 조회 작업을 성공한다.")
    @Test
    void list() {
        // given
        List<Menu> menus = Arrays.asList(메뉴세트(짜장면_탕수육_1인_메뉴_세트_요청, 1L), 메뉴세트(짬뽕_탕수육_1인_메뉴_세트_요청, 2L));
        when(menuRepository.findAll()).thenReturn(menus);

        // when
        List<MenuResponse> findMenus = menuService.list();

        // then
        assertAll(
                () -> assertThat(findMenus).hasSize(menus.size())
        );
    }
}
