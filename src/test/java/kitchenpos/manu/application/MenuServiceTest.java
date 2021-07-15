package kitchenpos.manu.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.NotFoundMenuGroupException;
import kitchenpos.menu.exception.NotFoundProductException;
import kitchenpos.menu.util.MenuMapper;
import kitchenpos.menu.util.MenuValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.wrap.Price;
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

@DisplayName("메뉴 기능 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    private MenuRequest 중식_요청;
    private Product 짜장면;
    private Product 탕수육;

    @Mock
    private MenuMapper menuMapper;

    @Mock
    private ProductRepository productRepository;

    private MenuValidator menuValidator;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    private MenuService menuService;

    @BeforeEach
    public void setUp() {
        menuValidator = new MenuValidator(productRepository);
        menuService = new MenuService(menuMapper, menuValidator, menuRepository, menuGroupRepository);

        // given
        // 중식에 포함된 메뉴가 생성되어 있음
        //중식_포함_메뉴 = new MenuProduct();
        짜장면 = new Product(1L,"짜장면", new Price(BigDecimal.valueOf(1000)));
    }

    @Test
    @DisplayName("존재 하지 않는 메뉴 그룹 ID에 등록 시 예외")
    void isNotExistMenuGroup_exception() {
        // given
        // 존재 하지 않는 메뉴 그룹 ID가 등록 되어 있음
        MenuProductRequest 중식_짜장면_요청 = new MenuProductRequest(1L, 1L);
        중식_요청 = new MenuRequest("중식", BigDecimal.valueOf(20000), 1L, Arrays.asList(중식_짜장면_요청));

        // then
        // 등록 요청 시 예외 발생
        when(menuGroupRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> menuService.create(중식_요청))
                .isInstanceOf(NotFoundMenuGroupException.class);
    }

    @Test
    @DisplayName("메뉴의 상품 리스트에 존재하지 않는 상품이 있음")
    void isNotExistProduct_exception() {
        // given
        // 등록되지 않은 상품이 등록되어 있음
        MenuProductRequest 중식_포함_메뉴_요청 = new MenuProductRequest(1L, 1L);

        // and
        // 메뉴에 등록되어 있음
        MenuProduct menuProduct = new MenuProduct(1L, 1L);
        MenuGroup menuGroup = new MenuGroup(1L);
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(menuProduct));
        Menu menu = new Menu("짜장면", new Price(BigDecimal.valueOf(20000)), menuGroup, menuProducts);
        중식_요청 = new MenuRequest("중식", BigDecimal.valueOf(20000), 1L, Arrays.asList(중식_포함_메뉴_요청));

        // when
        // 메뉴 그릅 등록되어 있음
        when(menuMapper.mapFormToMenu(중식_요청, menuGroup)).thenReturn(menu);
        when(menuGroupRepository.findById(1L)).thenReturn(Optional.of(menuGroup));

        // and
        // 상품이 존재하지 않음
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        // 등록 요청 시 예외 발생
        assertThatThrownBy(() -> menuService.create(중식_요청))
                .isInstanceOf(NotFoundProductException.class);
    }

    @Test
    @DisplayName("메뉴의 총 가격은 상품들의 가격의 합과 같거나 작아야 함")
    void isExpensiveMenuPriceSumThanProductAllPrice_exception() {
        // given
        // 상픔이 등록되어 있음
        짜장면 = 상품_생성(1L, "짜장면", 1000);
        탕수육 = 상품_생성(2L, "탕수육", 1000);
        MenuProduct 중식_짜장면_포함_메뉴 = new MenuProduct(1L, 1L);
        MenuProduct 중식_탕수육_포함_메뉴 = new MenuProduct(2L, 1L);
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(중식_짜장면_포함_메뉴, 중식_탕수육_포함_메뉴));

        // and
        // 메뉴가 등록되어 있음
        MenuProductRequest 중식_짜장면_포함_메뉴_요청 = new MenuProductRequest(1L, 1L);
        MenuProductRequest 중식_탕수육_포함_메뉴_요청 = new MenuProductRequest(2L, 1L);
        MenuGroup menuGroup = new MenuGroup(1L);
        Menu menu = new Menu("짜장면", new Price(BigDecimal.valueOf(21000)), menuGroup, menuProducts);
        중식_요청 = new MenuRequest("중식", BigDecimal.valueOf(2100), 1L, Arrays.asList(중식_짜장면_포함_메뉴_요청, 중식_탕수육_포함_메뉴_요청));

        // when
        // 메뉴 그릅과 상품이 등록되어 있음
        when(menuMapper.mapFormToMenu(중식_요청, menuGroup)).thenReturn(menu);
        when(menuGroupRepository.findById(1L)).thenReturn(Optional.of(menuGroup));

        // and
        // 제품 등록되어 있음
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(짜장면));
        when(productRepository.findById(2L)).thenReturn(Optional.ofNullable(탕수육));

        // then
        // 등록 요청 시 예외 발생
        assertThatThrownBy(() -> menuService.create(중식_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴_정상_등록_테스트")
    void 메뉴_정상_등록_테스트() {
        // given
        // 상픔이 등록되어 있음
        짜장면 = 상품_생성(1L, "짜장면", 1000);
        탕수육 = 상품_생성(2L, "탕수육", 1000);
        MenuProduct 중식_짜장면_포함_메뉴 = new MenuProduct(1L, 1L);
        MenuProduct 중식_탕수육_포함_메뉴 = new MenuProduct(2L, 1L);
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(중식_짜장면_포함_메뉴, 중식_탕수육_포함_메뉴));

        // and
        // 메뉴가 등록되어 있음
        MenuProductRequest 중식_짜장면_포함_메뉴_요청 = new MenuProductRequest(1L, 1L);
        MenuProductRequest 중식_탕수육_포함_메뉴_요청 = new MenuProductRequest(2L, 1L);
        MenuGroup menuGroup = new MenuGroup(1L);
        Menu menu = new Menu("짜장면", new Price(BigDecimal.valueOf(2000)), menuGroup, menuProducts);
        중식_요청 = new MenuRequest("중식", BigDecimal.valueOf(2000), 1L, Arrays.asList(중식_짜장면_포함_메뉴_요청, 중식_탕수육_포함_메뉴_요청));

        // when
        // 메뉴 그릅과 상품이 등록되어 있음
        when(menuMapper.mapFormToMenu(중식_요청, menuGroup)).thenReturn(menu);
        when(menuGroupRepository.findById(1L)).thenReturn(Optional.of(menuGroup));

        // and
        // 제품 등록되어 있음
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(짜장면));
        when(productRepository.findById(2L)).thenReturn(Optional.ofNullable(탕수육));
        when(menuRepository.save(menu)).thenReturn(menu);

        // then
        // 정상 등록 됨
        MenuResponse expected = menuService.create(중식_요청);
        assertThat(expected).isNotNull();
        assertThat(expected.getMenuProducts().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("메뉴_정상_조회_테스트")
    void 메뉴_정상_조회_테스트() {
        // given
        // 메뉴에 상픔이 등록되어 있음
        짜장면 = 상품_생성(1L, "짜장면", 1000);
        탕수육 = 상품_생성(2L, "탕수육", 1000);
        MenuProduct 중식_짜장면_포함_메뉴 = new MenuProduct(1L, 1L);
        MenuProduct 중식_탕수육_포함_메뉴 = new MenuProduct(2L, 1L);
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(중식_짜장면_포함_메뉴, 중식_탕수육_포함_메뉴));

        // and
        // 메뉴가 등록되어 있음
        MenuGroup menuGroup = new MenuGroup(1L);
        Menu menu = new Menu("짜장면", new Price(BigDecimal.valueOf(2000)), menuGroup, menuProducts);
        중식_짜장면_포함_메뉴.registerMenu(menu);
        중식_탕수육_포함_메뉴.registerMenu(menu);
        when(menuRepository.findAll()).thenReturn(Arrays.asList(menu));

        // then
        // 정상 조회 됨
        List<MenuResponse> expected = menuService.list();
        assertThat(expected.size()).isNotZero();
        assertThat(expected.get(0).getMenuProducts().size()).isNotZero();
    }

    private Product 상품_생성(Long id, String name, int price) {
        return new Product(id, name, new Price(BigDecimal.valueOf(price)));
    }

    private MenuProduct 메뉴_그룹_생성(Long productId, Long quantity) {
        return new MenuProduct(productId, quantity);
    }
}
