package kitchenpos.menu.ui;

import kitchenpos.exception.CannotFindException;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    MenuRepository menuRepository;

    @Mock
    MenuGroupRepository menuGroupRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    private final Long 메뉴_ID = 1L;
    private final Long 상품_ID = 1L;
    private final Long 메뉴그룹_ID = 1L;

    private final Product 후라이드 = new Product(1L, "후라이드", Price.valueOf(16000));
    private final Product 콜라 = new Product(2L, "콜라", Price.valueOf(1000));
    private final MenuGroup 인기메뉴 = new MenuGroup(메뉴그룹_ID, "인기메뉴");

    private final MenuProduct 후라이드_한마리 = new MenuProduct(후라이드.getId(), Quantity.of(1L));
    private final MenuProduct 콜라_한개 = new MenuProduct(콜라.getId(), Quantity.of(1L));
    private final List<MenuProduct> 메뉴상품_목록 = Arrays.asList(후라이드_한마리, 콜라_한개);
    private Menu 후라이드세트 = new Menu("후라이드세트", Price.valueOf(10000), 인기메뉴.getId(), 메뉴상품_목록);

    @DisplayName("0원 이상의 가격으로 메뉴를 등록한다")
    @Test
    void 메뉴_등록() {
        //Given
        MenuProductRequest 후라이드_한마리_요청 = new MenuProductRequest(메뉴_ID, 후라이드.getId(), 1L);
        MenuProductRequest 콜라_한개_요청 = new MenuProductRequest(메뉴_ID, 콜라.getId(), 1L);
        MenuRequest 후라이드세트_요청 = new MenuRequest(후라이드세트.getName(), 후라이드세트.getPrice().value(),
                메뉴그룹_ID, Arrays.asList(후라이드_한마리_요청, 콜라_한개_요청));

        when(menuGroupRepository.findById(인기메뉴.getId())).thenReturn(Optional.of(인기메뉴));
        when(productRepository.findAllById(Arrays.asList(후라이드.getId(), 콜라.getId()))).thenReturn(Arrays.asList(후라이드, 콜라));
        when(menuRepository.save(any())).thenReturn(후라이드세트);

        //When
        menuService.create(후라이드세트_요청);

        //Then
        verify(menuRepository, times(1)).save(any());
        verify(productRepository).findAllById(any());
        verify(menuGroupRepository).findById(any());
    }

    @DisplayName("메뉴 그룹이 기존에 등록되어 있지 않은 경우, 메뉴 등록시 예외가 발생한다")
    @Test
    void 메뉴그룹_등록되어있지_않은_경우_예외발생() {
        //Given
        Long 등록되지_않은_메뉴그룹_ID = 99L;
        MenuProductRequest 후라이드_한마리_요청 = new MenuProductRequest(메뉴_ID, 상품_ID, 1L);
        MenuRequest 후라이드세트_요청 = new MenuRequest(후라이드세트.getName(), 후라이드세트.getPrice().value(), 등록되지_않은_메뉴그룹_ID, Arrays.asList(후라이드_한마리_요청));

        //When + Then
        assertThatThrownBy(() -> menuService.create(후라이드세트_요청))
                .isInstanceOf(CannotFindException.class);
    }

    @DisplayName("메뉴 상품이 기존에 상품으로 등록되어 있지 않은 경우, 메뉴 등록시 예외가 발생한다")
    @Test
    void 메뉴상품_등록되어있지_않은_경우_예외발생() {
        //Given
        MenuProductRequest 후라이드_한마리_요청 = new MenuProductRequest(메뉴_ID, 상품_ID, 1L);
        MenuRequest 후라이드세트_요청 = new MenuRequest(후라이드세트.getName(), 후라이드세트.getPrice().value(), 인기메뉴.getId(), Arrays.asList(후라이드_한마리_요청));
        when(menuGroupRepository.findById(인기메뉴.getId())).thenReturn(Optional.of(인기메뉴));

        //When + Then
        assertThatThrownBy(() -> menuService.create(후라이드세트_요청))
                .isInstanceOf(CannotFindException.class);
    }

    @DisplayName("메뉴 목록을 조회할 수 있다")
    @Test
    void 메뉴_목록_조회() {
        //Given
        List<Menu> 입력한_메뉴_목록 = new ArrayList<>(Arrays.asList(후라이드세트));
        when(menuRepository.findAll()).thenReturn(입력한_메뉴_목록);

        //When
        List<MenuResponse> 조회된_메뉴_목록 = menuService.list();

        //Then
        verify(menuRepository, times(1)).findAll();
        assertThat(조회된_메뉴_목록).hasSize(입력한_메뉴_목록.size());

    }
}
