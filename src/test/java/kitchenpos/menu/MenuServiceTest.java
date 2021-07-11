package kitchenpos.menu;

import kitchenpos.exception.CannotFindException;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    private static Long 메뉴_ID = 1L;
    private static Long 상품_ID = 1L;
    private static Long 메뉴그룹_ID = 1L;
    private Product 후라이드;
    private MenuGroup 인기메뉴;
    private Menu 후라이드세트;
    private MenuProduct 후라이드_한마리;
    private MenuProducts 메뉴상품_목록;

    @BeforeEach
    void setUp() {
        후라이드 = new Product(상품_ID, "후라이드", BigDecimal.valueOf(16000));
        인기메뉴 = new MenuGroup(메뉴그룹_ID, "인기메뉴");
        후라이드_한마리 = new MenuProduct(후라이드, 1L);
        메뉴상품_목록 = new MenuProducts(Arrays.asList(후라이드_한마리));
        후라이드세트 = new Menu("후라이드세트", BigDecimal.valueOf(10000), 인기메뉴, 메뉴상품_목록);
    }

    @DisplayName("0원 이상의 가격으로 메뉴를 등록한다")
    @Test
    void 메뉴_등록() {
        //Given
        MenuProductRequest 후라이드_한마리_요청 = new MenuProductRequest(메뉴_ID, 상품_ID, 1L);
        MenuRequest 후라이드세트_요청 = new MenuRequest(후라이드세트.getName(), 후라이드세트.getPrice(),
                메뉴그룹_ID, Arrays.asList(후라이드_한마리_요청));

        when(menuGroupRepository.findById(인기메뉴.getId())).thenReturn(Optional.of(인기메뉴));
        when(productRepository.findAllById(Arrays.asList(후라이드.getId()))).thenReturn(Arrays.asList(후라이드));
        when(menuRepository.save(any())).thenReturn(후라이드세트);

        //When
        MenuResponse 생성된_메뉴 = menuService.create(후라이드세트_요청);

        //Then
        assertThat(생성된_메뉴.getName()).isEqualTo("후라이드세트");
    }

    @DisplayName("메뉴 그룹이 기존에 등록되어 있지 않은 경우, 메뉴 등록시 예외가 발생한다")
    @Test
    void 메뉴그룹_등록되어있지_않은_경우_예외발생() {
        //Given
        Long 등록되지_않은_메뉴그룹_ID = 99L;
        MenuProductRequest 후라이드_한마리_요청 = new MenuProductRequest(메뉴_ID, 상품_ID, 1L);
        MenuRequest 후라이드세트_요청 = new MenuRequest(후라이드세트.getName(), 후라이드세트.getPrice(), 등록되지_않은_메뉴그룹_ID, Arrays.asList(후라이드_한마리_요청));

        //When + Then
        assertThatThrownBy(() -> menuService.create(후라이드세트_요청))
                .isInstanceOf(CannotFindException.class);
    }

    @DisplayName("메뉴 상품이 기존에 상품으로 등록되어 있지 않은 경우, 메뉴 등록시 예외가 발생한다")
    @Test
    void 메뉴상품_등록되어있지_않은_경우_예외발생() {
        //Given
        MenuProductRequest 후라이드_한마리_요청 = new MenuProductRequest(메뉴_ID, 상품_ID, 1L);
        MenuRequest 후라이드세트_요청 = new MenuRequest(후라이드세트.getName(), 후라이드세트.getPrice(), 인기메뉴.getId(), Arrays.asList(후라이드_한마리_요청));
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
        assertThat(조회된_메뉴_목록).isNotNull()
                .hasSize(입력한_메뉴_목록.size());

    }
}
