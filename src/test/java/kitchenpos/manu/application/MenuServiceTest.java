package kitchenpos.manu.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.NotFoundMenuGroupException;
import kitchenpos.menu.exception.NotFoundProductException;
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
    private MenuProduct 중식_짜장면;
    private MenuProduct 중식_탕수육;
    private Product 짜장면;
    private Product 탕수육;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    @BeforeEach
    public void setUp() {
        // given
        // 중식에 포함된 메뉴가 생성되어 있음
        //중식_포함_메뉴 = new MenuProduct();
        짜장면 = new Product(1L,"짜장면", new Price(BigDecimal.valueOf(1000)));
    }

    @Test
    @DisplayName("메뉴 등록 금액 예외 테스트")
    void 메뉴_등록_금액_예외_테스트() {
        // given
        // 잘못 된 금액이 적용 되어 있음
        MenuProductRequest 중식_짜장면_요청 = new MenuProductRequest(1L, 1L);
        중식_요청 = new MenuRequest("중식", BigDecimal.valueOf(-100), 1L, Arrays.asList(중식_짜장면_요청));

        // when
        // 메뉴 그룹 등록되어 있음
        when(menuGroupRepository.findById(1L)).thenReturn(Optional.of(new MenuGroup("중식")));

        // and
        // 제품 등록되어 있음
        when(productRepository.findAllByIdIn(Arrays.asList(1L))).thenReturn(Optional.of(new ArrayList<Product>(Arrays.asList(짜장면))));

        // then
        // 등록 요청 시 예외 발생
        assertThatThrownBy(() -> menuService.create(중식_요청))
                .isInstanceOf(IllegalArgumentException.class);
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
        중식_요청 = new MenuRequest("중식", BigDecimal.valueOf(20000), 1L, Arrays.asList(중식_포함_메뉴_요청));

        // and
        // 메뉴 그릅 등록되어 있음
        when(menuGroupRepository.findById(1L)).thenReturn(Optional.of(new MenuGroup("중식")));

        // then
        // 등록 요청 시 예외 발생
        assertThatThrownBy(() -> menuService.create(중식_요청))
                .isInstanceOf(NotFoundProductException.class);
    }

    @Test
    @DisplayName("메뉴의 총 가격은 상품들의 가격의 합과 같거나 작아야 함")
    void isExpensiveMenuPriceSumThanProductAllPrice_exception() {
        // given
        // 메뉴에 상픔이 등록되어 있음
        짜장면 = 상품_생성(1L, "짜장면", 1000);
        탕수육 = 상품_생성(2L, "탕수육", 1000);
        MenuProductRequest 중식_짜장면_포함_메뉴_요청 = new MenuProductRequest(1L, 1L);
        MenuProductRequest 중식_탕수육_포함_메뉴_요청 = new MenuProductRequest(2L, 1L);

        // and
        // 메뉴에 등록되어 있음
        중식_요청 = new MenuRequest("중식", BigDecimal.valueOf(2100), 1L, Arrays.asList(중식_짜장면_포함_메뉴_요청, 중식_탕수육_포함_메뉴_요청));

        // and
        // 메뉴 그릅과 상품이 등록되어 있음
        when(menuGroupRepository.findById(1L)).thenReturn(Optional.of(new MenuGroup("중식")));

        // and
        // 제품 등록되어 있음
        when(productRepository.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Optional.of(new ArrayList<Product>(Arrays.asList(짜장면, 탕수육))));

        // then
        // 등록 요청 시 예외 발생
        assertThatThrownBy(() -> menuService.create(중식_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴_정상_등록_테스트")
    void 메뉴_정상_등록_테스트() {
        // given
        // 메뉴에 상픔이 등록되어 있음
        짜장면 = 상품_생성(1L, "짜장면", 1000);
        탕수육 = 상품_생성(2L, "탕수육", 1000);
        MenuProductRequest 중식_짜장면_포함_메뉴_요청 = new MenuProductRequest(1L, 1L);
        MenuProductRequest 중식_탕수육_포함_메뉴_요청 = new MenuProductRequest(2L, 1L);
        중식_짜장면 = new MenuProduct(짜장면, 1L);
        중식_탕수육 = new MenuProduct(탕수육, 1L);

        // and
        // 메뉴에 등록되어 있음
        중식_요청 = new MenuRequest("중식", BigDecimal.valueOf(2000), 1L, Arrays.asList(중식_짜장면_포함_메뉴_요청, 중식_탕수육_포함_메뉴_요청));
        Menu 중식 = new Menu(1L, "중식", new Price(BigDecimal.valueOf(2000)), new MenuGroup(1L, "중식메뉴"), new MenuProducts(Arrays.asList(중식_짜장면, 중식_탕수육)));

        // and
        // 메뉴 그릅과 상품이 등록되어 있음
        when(menuGroupRepository.findById(1L)).thenReturn(Optional.of(new MenuGroup("중식")));

        // and
        // 제품 등록되어 있음
        when(productRepository.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Optional.of(new ArrayList<Product>(Arrays.asList(짜장면, 탕수육))));

        // and
        // 메뉴와 메뉴에 등록된 상품들이 등록되어 있음
        when(menuRepository.save(any(Menu.class))).thenReturn(중식);

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
        중식_짜장면 = new MenuProduct(짜장면, 1L);
        중식_탕수육 = new MenuProduct(탕수육, 1L);

        // and
        // 메뉴에 등록되어 있음
        Menu 중식 = new Menu(1L, "중식", new Price(BigDecimal.valueOf(2000)), new MenuGroup(1L, "중식메뉴"), new MenuProducts(Arrays.asList(중식_짜장면, 중식_탕수육)));
        when(menuRepository.findAll()).thenReturn(Arrays.asList(중식));

        // then
        // 정상 조회 됨
        List<MenuResponse> expected = menuService.list();
        assertThat(expected.size()).isNotZero();
        assertThat(expected.get(0).getMenuProducts().size()).isNotZero();
    }

    private Product 상품_생성(Long id, String name, int price) {
        return new Product(id, name, new Price(BigDecimal.valueOf(price)));
    }

    private MenuProduct 메뉴_그룹_생성(Product product, Long quantity) {
        return new MenuProduct(product, quantity);
    }
}
