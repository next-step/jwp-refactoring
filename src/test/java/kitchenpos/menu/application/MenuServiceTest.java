package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.fixture.MenuFixture;
import kitchenpos.menu.fixture.MenuProductFixture;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.product.application.ProductService;
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
import java.util.NoSuchElementException;

import static kitchenpos.menugroup.fixture.MenuGroupFixture.추천_메뉴_그룹;
import static kitchenpos.product.fixture.ProductFixture.강정치킨;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupService menuGroupService;

    @Mock
    private ProductService productService;

    private MenuProduct 강정치킨_두마리;
    private Menu 강정치킨_두마리_세트_메뉴;

    @BeforeEach
    public void setUp() {
        강정치킨_두마리 = MenuProductFixture.create(1L, 강정치킨, 2);
        강정치킨_두마리_세트_메뉴 = MenuFixture.create(1L, "강정치킨_두마리_세트_메뉴", BigDecimal.valueOf(30_000), 추천_메뉴_그룹, Arrays.asList(강정치킨_두마리));

        강정치킨_두마리.setMenu(강정치킨_두마리_세트_메뉴);
    }

    @DisplayName("메뉴 생성 성공 테스트")
    @Test
    void create_success() {
        // given
        MenuProductRequest 요청_메뉴_상품 = MenuProductRequest.of(강정치킨.getId(), 2);
        MenuRequest 요청_메뉴 = MenuRequest.of("강정치킨_두마리_세트_메뉴", BigDecimal.valueOf(30_000), 추천_메뉴_그룹.getId(), Arrays.asList(요청_메뉴_상품));

        given(menuGroupService.findById(any(Long.class))).willReturn(추천_메뉴_그룹);
        given(productService.findById(any(Long.class))).willReturn(강정치킨);
        given(menuDao.save(any(Menu.class))).willReturn(강정치킨_두마리_세트_메뉴);

        // when
        MenuResponse 생성된_메뉴 = menuService.create(요청_메뉴);

        // then
        assertAll(
                () -> assertThat(생성된_메뉴).isEqualTo(MenuResponse.of(강정치킨_두마리_세트_메뉴))
                , () -> assertThat(생성된_메뉴.getMenuProducts()).containsExactly(MenuProductResponse.of(강정치킨_두마리))
        );
    }

    @DisplayName("메뉴 생성 실패 테스트 - 메뉴 가격이 0보다 작음")
    @Test
    void create_failure_invalidPrice() {
        // given
        MenuProductRequest 요청_메뉴_상품 = MenuProductRequest.of(강정치킨.getId(), 2);
        MenuRequest 요청_메뉴 = MenuRequest.of("강정치킨_두마리_세트_메뉴", BigDecimal.valueOf(-1), 추천_메뉴_그룹.getId(), Arrays.asList(요청_메뉴_상품));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(요청_메뉴));
    }

    @DisplayName("메뉴 생성 실패 테스트 - 메뉴의 상품이 존재하지 않음")
    @Test
    void create_failure_notFoundProduct() {
        // given
        MenuProductRequest 요청_메뉴_상품 = MenuProductRequest.of(강정치킨.getId(), 2);
        MenuRequest 요청_메뉴 = MenuRequest.of("강정치킨_두마리_세트_메뉴", BigDecimal.valueOf(30_000), 추천_메뉴_그룹.getId(), Arrays.asList(요청_메뉴_상품));

        given(menuGroupService.findById(추천_메뉴_그룹.getId())).willReturn(추천_메뉴_그룹);
        given(productService.findById(강정치킨.getId())).willThrow(new NoSuchElementException());

        // when & then
        assertThatThrownBy(() -> menuService.create(요청_메뉴))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("메뉴 생성 실패 테스트 - 메뉴가 메뉴 그룹에 속하지 않음")
    @Test
    void create_failure_notExistsMenuGroup() {
        // given
        MenuProductRequest 요청_메뉴_상품 = MenuProductRequest.of(강정치킨.getId(), 2);
        MenuRequest 요청_메뉴 = MenuRequest.of("강정치킨_두마리_세트_메뉴", BigDecimal.valueOf(30_000), 추천_메뉴_그룹.getId(), Arrays.asList(요청_메뉴_상품));

        given(menuGroupService.findById(추천_메뉴_그룹.getId())).willThrow(new NoSuchElementException());

        // when & then
        assertThatThrownBy(() -> menuService.create(요청_메뉴))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("메뉴 생성 실패 테스트 - 메뉴 가격이 각 메뉴 상품들 가격에 상품 수량을 곱해서 더한 금액을 초과")
    @Test
    void create_failure_exceedMenuPrice() {
        // given
        MenuProductRequest 요청_메뉴_상품 = MenuProductRequest.of(강정치킨.getId(), 2);
        MenuRequest 요청_메뉴 = MenuRequest.of("강정치킨_두마리_세트_메뉴", BigDecimal.valueOf(51_000), 추천_메뉴_그룹.getId(), Arrays.asList(요청_메뉴_상품));

        given(menuGroupService.findById(추천_메뉴_그룹.getId())).willReturn(추천_메뉴_그룹);
        given(productService.findById(강정치킨.getId())).willReturn(강정치킨);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(요청_메뉴));
    }

    @DisplayName("메뉴 목록 조회 테스트")
    @Test
    void list() {
        // given
        given(menuDao.findAll()).willReturn(Arrays.asList(강정치킨_두마리_세트_메뉴));

        // when
        List<MenuResponse> 조회된_메뉴_목록 = menuService.list();

        // then
        assertThat(조회된_메뉴_목록).containsExactly(MenuResponse.of(강정치킨_두마리_세트_메뉴));
    }
}
