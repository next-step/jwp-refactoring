package kitchenpos.menu.application;

import kitchenpos.common.exception.InvalidPriceException;
import kitchenpos.common.exception.NotFoundMenuGroupException;
import kitchenpos.common.exception.NotFoundProductException;
import kitchenpos.common.exception.OverMenuPriceException;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static kitchenpos.menu.domain.MenuGroupTest.두마리메뉴;
import static kitchenpos.menu.domain.MenuTest.양념치킨_단품;
import static kitchenpos.menu.domain.MenuTest.치킨세트;
import static kitchenpos.product.domain.ProductTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 관리 테스트")
public class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MenuProductService menuProductService;
    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("메뉴 등록")
    void createTest() {
        // given
        MenuProductRequest 간장치킨_요청 = new MenuProductRequest(간장치킨_상품.getId(), 2L);
        MenuRequest 요청_데이터 = new MenuRequest(
                "간장 두마리 세트"
                , BigDecimal.valueOf(32_000)
                , 두마리메뉴.getId()
                , Collections.singletonList(간장치킨_요청));
        Menu 간장_두마리_세트 = 요청_데이터.toMenu(두마리메뉴);
        MenuProducts menuProducts = MenuProducts.of(Arrays.asList(new MenuProduct(간장치킨_상품, 2L)));
        given(menuProductService.allSave(anyList(), any())).willReturn(menuProducts);
        given(menuGroupRepository.findByIdElseThrow(anyLong())).willReturn(두마리메뉴);
        given(productRepository.findByIdElseThrow(anyLong())).willReturn(간장치킨_상품);
        given(menuRepository.save(any(Menu.class))).willReturn(간장_두마리_세트);
        // when
        MenuResponse actual = menuService.create(요청_데이터);
        // then
        verify(menuRepository, times(1)).save(any());
        assertThat(actual.getName()).isEqualTo(요청_데이터.getName());
    }

    @Test
    @DisplayName("메뉴 가격은 0원 이상 이어야 한다.")
    void menuPriceOverZeroExceptionTest() {
        // given
        MenuProductRequest 간장치킨_요청 = new MenuProductRequest(간장치킨_상품.getId(), 2L);
        MenuRequest 요청_데이터 = new MenuRequest(
                "간장 두마리 세트"
                , BigDecimal.valueOf(-1)
                , 두마리메뉴.getId()
                , Collections.singletonList(간장치킨_요청));
        given(menuGroupRepository.findByIdElseThrow(anyLong())).willReturn(두마리메뉴);
        // when
        // then
        assertThatThrownBy(() -> menuService.create(요청_데이터))
                .isInstanceOf(InvalidPriceException.class);
    }

    @Test
    @DisplayName("메뉴 그룹이 존재하지 않으면 등록 할 수 없다.")
    void notFoundMenuGroupExceptionTest() {
        // given
        MenuProductRequest 간장치킨_요청 = new MenuProductRequest(간장치킨_상품.getId(), 2L);
        MenuRequest 요청_데이터 = new MenuRequest(
                "간장 두마리 세트"
                , BigDecimal.valueOf(32_000)
                , 7L
                , Collections.singletonList(간장치킨_요청));
        given(menuGroupRepository.findByIdElseThrow(anyLong())).willThrow(NotFoundMenuGroupException.class);
        // when
        // then
        assertThatThrownBy(() -> menuService.create(요청_데이터))
                .isInstanceOf(NotFoundMenuGroupException.class);
    }

    @Test
    @DisplayName("메뉴에 포함된 상품들이 존재하지 않으면 등록 할 수 없다.")
    void notFoundMenuProductTest() {
        // given
        MenuProductRequest 간장치킨_요청 = new MenuProductRequest(간장치킨_상품.getId(), 2L);
        MenuRequest 요청_데이터 = new MenuRequest(
                "간장 두마리 세트"
                , BigDecimal.valueOf(32_000)
                , 두마리메뉴.getId()
                , Collections.singletonList(간장치킨_요청));
        given(productRepository.findByIdElseThrow(anyLong())).willThrow(NotFoundProductException.class);
        // when
        // then
        assertThatThrownBy(() -> menuService.create(요청_데이터))
                .isInstanceOf(NotFoundProductException.class);
    }

    @Test
    @DisplayName("메뉴 가격은 [`상품 가격 * 메뉴에 속하는 상품 수량`]의 합보다 클 수 없다.")
    void menuPriceNotOverTotalProductPriceTest() {
        // given
        MenuProductRequest 간장치킨_요청 = new MenuProductRequest(간장치킨_상품.getId(), 1L);
        MenuProductRequest 순살치킨_요청 = new MenuProductRequest(순살치킨_상품.getId(), 1L);
        MenuRequest 요청_데이터 = new MenuRequest(
                "치킨 두마리 세트"
                , BigDecimal.valueOf(34_001)
                , 두마리메뉴.getId()
                , Arrays.asList(간장치킨_요청, 순살치킨_요청));
        given(menuGroupRepository.findByIdElseThrow(anyLong())).willReturn(두마리메뉴);
        given(productRepository.findByIdElseThrow(anyLong())).willReturn(간장치킨_상품, 순살치킨_상품);
        // when
        // then
        assertThatThrownBy(() -> menuService.create(요청_데이터))
                .isInstanceOf(OverMenuPriceException.class);
    }

    @Test
    @DisplayName("메뉴 리스트 조회")
    void listTest() {
        // given
        given(menuRepository.findAll()).willReturn(Arrays.asList(치킨세트, 양념치킨_단품));
        // when
        List<MenuResponse> actual = menuService.list();
        // then
        verify(menuRepository, only()).findAll();
        assertThat(actual).hasSize(2);
    }
}
