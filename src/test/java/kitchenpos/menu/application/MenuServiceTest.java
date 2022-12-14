package kitchenpos.menu.application;

import static kitchenpos.menu.domain.MenuTestFixture.*;
import static kitchenpos.menu.domain.MenuGroupTestFixture.*;
import static kitchenpos.menu.domain.MenuProductTestFixture.*;
import static kitchenpos.product.domain.ProductTestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 비즈니스 로직 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private ProductService productService;
    @Mock
    private MenuGroupService menuGroupService;
    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("가격이 없는 메뉴를 등록한다.")
    void createMenuByPriceIsNull() {
        // given
        MenuRequest 짜장1 = menuRequest("짜장1", null, 1L, 짜장면_1그릇_요청);
        given(productService.findById(짜장면_1그릇_요청.getProductId())).willReturn(짜장면);

        // when & then
        assertThatThrownBy(() -> menuService.create(짜장1))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("가격은 필수입니다.");
    }

    @Test
    @DisplayName("가격이 0원 이하인 메뉴를 등록한다.")
    void createMenuByPriceLessThanZero() {
        // given
        MenuRequest 짜장1 = menuRequest("짜장1", BigDecimal.valueOf(-2_000L), 1L, 짜장면_1그릇_요청);
        given(productService.findById(짜장면_1그릇_요청.getProductId())).willReturn(짜장면);

        // when & then
        assertThatThrownBy(() -> menuService.create(짜장1))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @Test
    @DisplayName("메뉴 그룹에 속해있지 않은 메뉴를 등록한다.")
    void createMenuByMenuGroupNotExist() {
        // given
        MenuRequest 탕수육소1 = menuRequest("탕수육소1", BigDecimal.valueOf(18_000L), 3L, 탕수육_소_1그릇_요청);
        given(menuGroupService.findById(3L)).willReturn(null);
        given(productService.findById(탕수육_소_1그릇_요청.getProductId())).willReturn(탕수육_소);

        // when & then
        assertThatThrownBy(() -> menuService.create(탕수육소1))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("메뉴 그룹은 필수입니다.");
    }

    @Test
    @DisplayName("상품으로 등록되어 있지 않은 메뉴를 등록한다.")
    void createMenuByNotCreateProduct() {
        // given
        MenuRequest 탕수육소1 = menuRequest("탕수육소1", BigDecimal.valueOf(18_000L), 3L, 탕수육_소_1그릇_요청);
        given(menuGroupService.findById(3L)).willReturn(요리류);
        given(productService.findById(탕수육_소_1그릇_요청.getProductId())).willReturn(null);

        // when & then
        assertThatThrownBy(() -> menuService.create(탕수육소1))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("상품은 필수입니다.");
    }

    @Test
    @DisplayName("메뉴 가격은 메뉴 상품가격의 합계보다 클 수 없다.")
    void createMenuByNotMoreThanProductsSum() {
        // given
        MenuRequest 짜장1_짬뽕2_요청 = menuRequest("짜장1_짬뽕2_요청", BigDecimal.valueOf(25_000L), 1L, 짜장면_1그릇_요청, 짬뽕_2그릇_요청);
        given(productService.findById(짜장면_1그릇_요청.getProductId())).willReturn(짜장면);
        given(productService.findById(짬뽕_2그릇_요청.getProductId())).willReturn(짬뽕);
        given(menuGroupService.findById(1L)).willReturn(면류);

        // when & then
        assertThatThrownBy(() -> menuService.create(짜장1_짬뽕2_요청))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("상품 총 금액이 메뉴의 가격 보다 클 수 없습니다.");
    }

    @Test
    @DisplayName("메뉴를 등록한다.")
    void createMenu() {
        // given
        Menu 짜장1_짬뽕2 = menu(1L, "짜장1_짬뽕2", BigDecimal.valueOf(23_000L), 면류, 짜장면_1그릇, 짬뽕_2그릇);
        MenuRequest 짜장1_짬뽕2_요청 = menuRequest("짜장1_짬뽕2_요청", BigDecimal.valueOf(23_000L), 1L, 짜장면_1그릇_요청, 짬뽕_2그릇_요청);
        given(productService.findById(짜장면_1그릇_요청.getProductId())).willReturn(짜장면);
        given(productService.findById(짬뽕_2그릇_요청.getProductId())).willReturn(짬뽕);
        given(menuGroupService.findById(1L)).willReturn(면류);
        given(menuRepository.save(any())).willReturn(짜장1_짬뽕2);

        // when
        MenuResponse actual = menuService.create(짜장1_짬뽕2_요청);

        // then
        assertAll(
                () -> assertThat(actual.getName()).isEqualTo("짜장1_짬뽕2"),
                () -> assertThat(actual.getPrice()).isEqualTo(BigDecimal.valueOf(23000))
        );
    }

    @Test
    @DisplayName("메뉴 목록을 조회하면 메뉴 목록이 반환된다.")
    void findMenus() {
        // given
        Menu 짜장1 = menu(1L, "짜장1", BigDecimal.valueOf(7000), 면류, 짜장면_1그릇);
        Menu 짬뽕2 = menu(2L, "짬뽕2", BigDecimal.valueOf(16000), 면류, 짬뽕_2그릇);
        List<Menu> menus = Arrays.asList(짜장1, 짬뽕2);
        given(menuRepository.findAll()).willReturn(menus);

        // when
        List<MenuResponse> actual = menuService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual.stream()
                        .map(MenuResponse::getName)
                        .collect(Collectors.toList())).containsExactly("짜장1", "짬뽕2")
        );
    }
}
