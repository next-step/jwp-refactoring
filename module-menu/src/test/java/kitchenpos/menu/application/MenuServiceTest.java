package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.fixture.MenuFixture;
import kitchenpos.menu.fixture.MenuProductFixture;
import org.assertj.core.api.Assertions;
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

import static kitchenpos.menugroup.fixture.MenuGroupFixture.추천_메뉴_그룹;
import static kitchenpos.product.fixture.ProductFixture.강정치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuValidator menuValidator;

    private MenuProduct 강정치킨_두마리;
    private Menu 강정치킨_두마리_세트_메뉴;

    @BeforeEach
    public void setUp() {
        강정치킨_두마리 = MenuProductFixture.create(1L, 강정치킨.getId(), 2);
        강정치킨_두마리_세트_메뉴 = MenuFixture.create(1L, "강정치킨_두마리_세트_메뉴", BigDecimal.valueOf(30_000), 추천_메뉴_그룹.getId(), Arrays.asList(강정치킨_두마리));

        강정치킨_두마리.setMenu(강정치킨_두마리_세트_메뉴);
    }

    @DisplayName("메뉴 생성 성공 테스트")
    @Test
    void create_success() {
        // given
        MenuProductRequest 요청_메뉴_상품 = MenuProductRequest.of(강정치킨.getId(), 2);
        MenuRequest 요청_메뉴 = MenuRequest.of("강정치킨_두마리_세트_메뉴", BigDecimal.valueOf(30_000), 추천_메뉴_그룹.getId(), Arrays.asList(요청_메뉴_상품));

        given(menuRepository.save(any(Menu.class))).willReturn(강정치킨_두마리_세트_메뉴);

        // when
        MenuResponse 생성된_메뉴 = menuService.create(요청_메뉴);

        // then
        assertAll(
                () -> assertThat(생성된_메뉴).isEqualTo(MenuResponse.of(강정치킨_두마리_세트_메뉴))
                , () -> assertThat(생성된_메뉴.getMenuProducts()).containsExactly(MenuProductResponse.of(강정치킨_두마리))
        );
    }

    @DisplayName("메뉴 생성 실패 테스트 - 메뉴 이름이 공백")
    @Test
    void create_failure_invalidName() {
        // given
        MenuProductRequest 요청_메뉴_상품 = MenuProductRequest.of(강정치킨.getId(), 2);
        MenuRequest 요청_메뉴 = MenuRequest.of("", BigDecimal.valueOf(-1), 추천_메뉴_그룹.getId(), Arrays.asList(요청_메뉴_상품));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(요청_메뉴));
    }

    @DisplayName("메뉴 생성 실패 테스트 - 메뉴 가격이 null")
    @Test
    void create_failure_invalidPrice_null() {
        // given
        MenuProductRequest 요청_메뉴_상품 = MenuProductRequest.of(강정치킨.getId(), 2);
        MenuRequest 요청_메뉴 = MenuRequest.of("강정치킨_두마리_세트_메뉴", null, 추천_메뉴_그룹.getId(), Arrays.asList(요청_메뉴_상품));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(요청_메뉴));
    }

    @DisplayName("메뉴 생성 실패 테스트 - 메뉴 가격이 0보다 작음")
    @Test
    void create_failure_invalidPrice_negative() {
        // given
        MenuProductRequest 요청_메뉴_상품 = MenuProductRequest.of(강정치킨.getId(), 2);
        MenuRequest 요청_메뉴 = MenuRequest.of("강정치킨_두마리_세트_메뉴", BigDecimal.valueOf(-1), 추천_메뉴_그룹.getId(), Arrays.asList(요청_메뉴_상품));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(요청_메뉴));
    }

    @DisplayName("메뉴 목록 조회 테스트")
    @Test
    void list() {
        // given
        given(menuRepository.findAll()).willReturn(Arrays.asList(강정치킨_두마리_세트_메뉴));

        // when
        List<MenuResponse> 조회된_메뉴_목록 = menuService.list();

        // then
        Assertions.assertThat(조회된_메뉴_목록).containsExactly(MenuResponse.of(강정치킨_두마리_세트_메뉴));
    }
}
