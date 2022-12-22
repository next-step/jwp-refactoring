package kitchenpos.menu.application;

import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuGroupService menuGroupService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuRepository menuRepository;


    private Product 상품_후라이드치킨;
    private MenuProduct 메뉴_상품_후라이드_치킨;
    private MenuProductRequest 메뉴_상품_후라이드_치킨_요청;

    private Menu 메뉴_기본;
    private MenuGroup 메뉴_그룹_치킨;
    private MenuRequest 메뉴_기본_요청;

    @BeforeEach
    void set_up() {
        메뉴_그룹_치킨 = MenuGroupFixture.create("메뉴 그룹 기본");
        메뉴_기본 = MenuFixture.create("메뉴 기본", BigDecimal.valueOf(15_000), 메뉴_그룹_치킨);
        상품_후라이드치킨 = ProductFixture.create("후라이드치킨", BigDecimal.valueOf(15_000));
        ReflectionTestUtils.setField(상품_후라이드치킨, "id", 1L);

        메뉴_상품_후라이드_치킨_요청 = new MenuProductRequest(1L, 2L);
        메뉴_기본_요청 = new MenuRequest("메뉴 기본", BigDecimal.valueOf(15_000), 메뉴_그룹_치킨.getId(), Arrays.asList(메뉴_상품_후라이드_치킨_요청));
    }

    @DisplayName("메뉴를 등록할 수 있다")
    @Test
    void create() {
        // given
        when(menuGroupService.findById(any())).thenReturn(메뉴_그룹_치킨);
        BDDMockito.given(productRepository.findAllById(Arrays.asList(1L))).willReturn(Arrays.asList(상품_후라이드치킨));
        when(menuRepository.save(any())).thenReturn(메뉴_기본);

        // when
        MenuResponse 메뉴_기본_등록 = menuService.create(메뉴_기본_요청);

        // then
        assertThat(메뉴_기본_등록.getName()).isEqualTo(메뉴_기본.getName());
    }


    @DisplayName("메뉴의 가격이 0원 이상이어야 한다.")
    @Test
    void create_error_menu_price_minus() {
        // given
        MenuRequest 메뉴_가격_이상 = new MenuRequest(
                "메뉴 기본", BigDecimal.valueOf(-15_000), 메뉴_그룹_치킨.getId(),  Arrays.asList(메뉴_상품_후라이드_치킨_요청)
        );
        when(menuGroupService.findById(any())).thenReturn(메뉴_그룹_치킨);
        when(productRepository.findAllById(Arrays.asList(1L))).thenReturn(Arrays.asList(상품_후라이드치킨));

        // when && then
        assertThatThrownBy(() -> menuService.create(메뉴_가격_이상))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴그룹이 없다면 메뉴를 등록할 수 없다.")
    @Test
    void create_error_no_exist_menu_group() {
        // given
        when(menuGroupService.findById(메뉴_그룹_치킨.getId())).thenThrow(IllegalArgumentException.class);

        // when && then
        assertThatThrownBy(() -> menuService.create(메뉴_기본_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록 조회 시 메뉴 상품도 함께 조회한다.")
    @Test
    void list() {
        // given
        when(menuRepository.findAll()).thenReturn(Arrays.asList(메뉴_기본));

        // when
        List<MenuResponse> 메뉴_목록_조회 = menuService.list();

        // then
        assertAll(
                () -> assertThat(메뉴_목록_조회).hasSize(1),
                () -> assertThat(메뉴_목록_조회.get(0).getName()).isEqualTo(메뉴_기본.getName())
        );
    }

    @DisplayName("메뉴의 가격이 메뉴 상품 가격의 합보다 크다면 등록할 수 없다")
    @Test
    void menu_price_less_then_menu_product_sum() {
        // given
        when(menuGroupService.findById(any())).thenReturn(메뉴_그룹_치킨);
        when(productRepository.findAllById(Arrays.asList(1L))).thenReturn(Arrays.asList(상품_후라이드치킨));
        MenuRequest 비싼_메뉴가격_기본_요청 = new MenuRequest("메뉴 기본", BigDecimal.valueOf(999_999), 메뉴_그룹_치킨.getId(), Arrays.asList(메뉴_상품_후라이드_치킨_요청));

        // when && then
        Assertions.assertThatThrownBy(() -> menuService.create(비싼_메뉴가격_기본_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
