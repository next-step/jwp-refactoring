package kitchenpos.menu.application;

import static kitchenpos.common.exception.ErrorCode.PRICE_GREATER_THAN_SUM;
import static kitchenpos.common.exception.ErrorCode.PRICE_IS_NULL_OR_MINUS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.common.exception.KitchenposException;
import kitchenpos.menu.application.validator.MenuValidator;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.request.MenuRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.menu.domain.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuValidator menuValidator;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupService menuGroupService;
    @InjectMocks
    private MenuService menuService;
    private Product 후라이드;
    private MenuProductRequest 메뉴에_등록된_상품;
    private MenuRequest 메뉴;
    private MenuGroup 메뉴_그룹;

    @BeforeEach
    void setUp() {
        후라이드 = new Product(1L, "후라이드", BigDecimal.valueOf(16000));
        메뉴에_등록된_상품 = new MenuProductRequest(후라이드.getId(), 1);
        메뉴_그룹 = new MenuGroup(1L, "한마리메뉴");
        메뉴 = new MenuRequest(
                "후라이드치킨", BigDecimal.valueOf(16000), 메뉴_그룹.getId(), Arrays.asList(메뉴에_등록된_상품));
    }

    @Test
    void 생성() {
        given(menuGroupService.findById(anyLong())).willReturn(메뉴_그룹);
        doNothing().when(menuValidator).validateCreate(any());
        given(menuRepository.save(any())).willReturn(메뉴.toEntity());

        MenuResponse response = menuService.create(메뉴);

        assertAll(
                () -> assertThat(response.getName()).isEqualTo("후라이드치킨"),
                () -> assertThat(response.getPrice()).isEqualTo(BigDecimal.valueOf(16000)),
                () -> assertThat(response.getMenuGroupId()).isEqualTo(1L),
                () -> assertThat(response.getMenuProducts()).isNotEmpty()
        );
    }

    @Test
    void 메뉴_가격이_0미만인_경우() {
        doThrow(new KitchenposException(PRICE_IS_NULL_OR_MINUS)).when(menuValidator).validateCreate(any());
        메뉴 = new MenuRequest(
                "양념치킨", BigDecimal.valueOf(-5000), 메뉴_그룹.getId(), Arrays.asList(메뉴에_등록된_상품));

        assertThatThrownBy(
                () -> menuService.create(메뉴)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(PRICE_IS_NULL_OR_MINUS.getDetail());
    }

    @ParameterizedTest
    @NullSource
    void 메뉴_가격이_null인_경우(BigDecimal price) {
        given(menuGroupService.findById(anyLong())).willReturn(메뉴_그룹);
        doThrow(new KitchenposException(PRICE_IS_NULL_OR_MINUS)).when(menuValidator).validateCreate(any());
        메뉴 = new MenuRequest("양념치킨", price, 메뉴_그룹.getId(), Arrays.asList(메뉴에_등록된_상품));

        assertThatThrownBy(
                () -> menuService.create(메뉴)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(PRICE_IS_NULL_OR_MINUS.getDetail());
    }

    @Test
    void 메뉴의_가격이_등록된_상품의_가격보다_큰_경우() {
        후라이드 = new Product(1L, "후라이드", BigDecimal.valueOf(6000));
        given(menuGroupService.findById(anyLong())).willReturn(메뉴_그룹);
        doThrow(new KitchenposException(PRICE_GREATER_THAN_SUM)).when(menuValidator).validateCreate(any());

        assertThatThrownBy(
                () -> menuService.create(메뉴)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(PRICE_GREATER_THAN_SUM.getDetail());
    }

    @Test
    void 메뉴의_가격이_메뉴에_등록된_상품_가격의_합계보다_큰_경우() {
        메뉴에_등록된_상품 = new MenuProductRequest(후라이드.getId(), 0);
        메뉴 = new MenuRequest("후라이드치킨", BigDecimal.valueOf(16000), 메뉴_그룹.getId(), Arrays.asList(메뉴에_등록된_상품));

        given(menuGroupService.findById(anyLong())).willReturn(메뉴_그룹);
        doThrow(new KitchenposException(PRICE_GREATER_THAN_SUM)).when(menuValidator).validateCreate(any());

        assertThatThrownBy(
                () -> menuService.create(메뉴)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(PRICE_GREATER_THAN_SUM.getDetail());
    }

    @Test
    void 조회() {
        given(menuRepository.findAll()).willReturn(Arrays.asList(메뉴.toEntity()));

        List<MenuResponse> response = menuService.list();

        assertAll(
                () -> assertThat(response.size()).isEqualTo(1),
                () -> assertThat(response.get(0).getMenuProducts()).isNotEmpty()
        );
    }
}
