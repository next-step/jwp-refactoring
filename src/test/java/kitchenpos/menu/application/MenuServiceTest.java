package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menu.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    private Product 첫번째_상품;
    private Product 두번째_상품;
    private MenuProduct 첫번째_메뉴상품;
    private MenuProduct 두번째_메뉴상품;
    private List<MenuProduct> 메뉴상품_목록;
    private MenuGroup 메뉴그룹;
    private Menu 생성할_메뉴;

    @BeforeEach
    void setUp() {
        첫번째_상품 = new Product(1L, "짬뽕", BigDecimal.valueOf(9000));
        두번째_상품 = new Product(2L, "짜장면", BigDecimal.valueOf(8000));
        첫번째_메뉴상품 = new MenuProduct(1L, 첫번째_상품, 2);
        두번째_메뉴상품 = new MenuProduct(2L, 두번째_상품,1);
        메뉴상품_목록 = Arrays.asList(첫번째_메뉴상품, 두번째_메뉴상품);
        메뉴그룹 = new MenuGroup(1L, "추천메뉴");
        생성할_메뉴 = new Menu(1L, "짬짜면세트", BigDecimal.valueOf(26000), 메뉴그룹, 메뉴상품_목록);
    }

    @DisplayName("메뉴를 생성할 수 있다.")
    @Test
    void createMenu() {
        // given
        List<MenuProductRequest> 메뉴상품_요청_목록 = Arrays.asList(
                new MenuProductRequest(1L, 2),
                new MenuProductRequest(2L, 1));
        MenuRequest 생성할_메뉴_요청 = new MenuRequest("짬짜면세트", BigDecimal.valueOf(26000), 1L, 메뉴상품_요청_목록);

        given(productRepository.findById(1L))
                .willReturn(Optional.of(첫번째_상품));
        given(productRepository.findById(2L))
                .willReturn(Optional.of(두번째_상품));
        given(menuGroupRepository.findById(any()))
                .willReturn(Optional.of(메뉴그룹));
        given(menuRepository.save(any()))
                .willReturn(생성할_메뉴);

        // when
        MenuResponse 생성된_메뉴_응답 = menuService.create(생성할_메뉴_요청);

        // then
        메뉴_생성_성공(생성된_메뉴_응답, 생성할_메뉴_요청);
    }

    @DisplayName("메뉴 가격 정보가 없으면 메뉴 생성에 실패한다.")
    @Test
    void createMenuFailsWhenNullPrice() {
        // given
        List<MenuProductRequest> 메뉴상품_요청_목록 = Arrays.asList(
                new MenuProductRequest(1L, 2), new MenuProductRequest(2L, 1));
        MenuRequest 생성할_메뉴_요청 = new MenuRequest("짬짜면세트", null, 1L, 메뉴상품_요청_목록);

        given(productRepository.findById(1L))
                .willReturn(Optional.of(첫번째_상품));
        given(productRepository.findById(2L))
                .willReturn(Optional.of(두번째_상품));
        given(menuGroupRepository.findById(any()))
                .willReturn(Optional.of(메뉴그룹));

        // when & then
        메뉴_생성_실패_가격정보_오류(생성할_메뉴_요청);
    }

    @DisplayName("메뉴 가격이 0 미만이면 메뉴 생성에 실패한다.")
    @Test
    void createMenuFailsWhenMinusPrice() {
        // given
        List<MenuProductRequest> 메뉴상품_요청_목록 = Arrays.asList(
                new MenuProductRequest(1L, 2), new MenuProductRequest(2L, 1));
        MenuRequest 생성할_메뉴_요청 = new MenuRequest("짬짜면세트", BigDecimal.valueOf(-26000), 1L, 메뉴상품_요청_목록);

        given(productRepository.findById(1L))
                .willReturn(Optional.of(첫번째_상품));
        given(productRepository.findById(2L))
                .willReturn(Optional.of(두번째_상품));
        given(menuGroupRepository.findById(any()))
                .willReturn(Optional.of(메뉴그룹));

        // when & then
        메뉴_생성_실패_가격정보_오류(생성할_메뉴_요청);
    }

    @DisplayName("메뉴 그룹이 존재하지 않으면 메뉴 생성에 실패한다.")
    @Test
    void createMenuFailsWhenNoMenuGroup() {
        // given
        Long 존재하지_않는_메뉴그룹ID = 1000L;
        List<MenuProductRequest> 메뉴상품_요청_목록 = Arrays.asList(
                new MenuProductRequest(1L, 2), new MenuProductRequest(2L, 1));
        MenuRequest 생성할_메뉴_요청 = new MenuRequest("짬짜면세트", BigDecimal.valueOf(-26000), 존재하지_않는_메뉴그룹ID, 메뉴상품_요청_목록);

        given(productRepository.findById(1L))
                .willReturn(Optional.of(첫번째_상품));
        given(productRepository.findById(2L))
                .willReturn(Optional.of(두번째_상품));

        // when & then
        메뉴_생성_실패_메뉴_그룹_없음(생성할_메뉴_요청);
    }

    @DisplayName("메뉴의 상품이 존재하지 않으면 메뉴 생성에 실패한다.")
    @Test
    void createMenuFailsWhenNoProduct() {
        // given
        Long 존재하지_않는_상품ID = 1000L;
        List<MenuProductRequest> 메뉴상품_요청_목록 = Arrays.asList(new MenuProductRequest(존재하지_않는_상품ID, 2));
        MenuRequest 생성할_메뉴_요청 = new MenuRequest("짬짜면세트", BigDecimal.valueOf(-26000), 1L, 메뉴상품_요청_목록);

        // when & then
        메뉴_생성_실패_상품_없음(생성할_메뉴_요청);
    }

    @DisplayName("메뉴의 가격이 메뉴에 포함된 상품들의 금액 합보다 크면 메뉴 생성에 실패한다.")
    @Test
    void createMenuFailsWhenInvalidPrice() {
        // given
        List<MenuProductRequest> 메뉴상품_요청_목록 = Arrays.asList(
                new MenuProductRequest(1L, 2), new MenuProductRequest(2L, 1));
        MenuRequest 생성할_메뉴_요청 = new MenuRequest("짬짜면세트", BigDecimal.valueOf(27000), 1L, 메뉴상품_요청_목록);

        given(productRepository.findById(1L))
                .willReturn(Optional.of(첫번째_상품));
        given(productRepository.findById(2L))
                .willReturn(Optional.of(두번째_상품));
        given(menuGroupRepository.findById(any()))
                .willReturn(Optional.of(메뉴그룹));

        // when & then
        메뉴_생성_실패_메뉴_가격_초과(생성할_메뉴_요청);
    }

    @DisplayName("메뉴 목록 조회를 할 수 있다.")
    @Test
    void listMenu() {
        // given
        List<Menu> 조회할_메뉴_목록 = Arrays.asList(생성할_메뉴);
        given(menuRepository.findAll())
                .willReturn(조회할_메뉴_목록);

        // when
        List<MenuResponse> 조회된_메뉴_응답_목록 = menuService.list();

        // then
        메뉴_목록_조회_성공(조회된_메뉴_응답_목록, 조회할_메뉴_목록);
    }

    private void 메뉴_생성_성공(MenuResponse 생성된_메뉴, MenuRequest 생성할_메뉴) {
        assertAll(
                () -> assertThat(생성된_메뉴.getId())
                        .isNotNull(),
                () -> assertThat(생성된_메뉴.getName())
                        .isEqualTo(생성할_메뉴.getName()),
                () -> assertThat(생성된_메뉴.getPrice())
                        .isEqualTo(생성할_메뉴.getPrice()),
                () -> assertThat(생성된_메뉴.getMenuGroupId())
                        .isEqualTo(생성할_메뉴.getMenuGroupId())
        );
    }

    private void 메뉴_생성_실패_가격정보_오류(MenuRequest 생성할_메뉴_요청) {
        assertThatThrownBy(() -> menuService.create(생성할_메뉴_요청))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0보다 커야 합니다.");
    }

    private void 메뉴_생성_실패_메뉴_그룹_없음(MenuRequest 생성할_메뉴_요청) {
        assertThatThrownBy(() -> menuService.create(생성할_메뉴_요청))
                .isExactlyInstanceOf(MenuGroupNotFoundException.class)
                .hasMessage("메뉴 그룹이 존재하지 않습니다.");
    }

    private void 메뉴_생성_실패_상품_없음(MenuRequest 생성할_메뉴_요청) {
        assertThatThrownBy(() -> menuService.create(생성할_메뉴_요청))
                .isExactlyInstanceOf(ProductNotFoundException.class)
                .hasMessage("상품이 존재하지 않습니다.");
    }

    private void 메뉴_생성_실패_메뉴_가격_초과(MenuRequest 생성할_메뉴_요청) {
        assertThatThrownBy(() -> menuService.create(생성할_메뉴_요청))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("메뉴의 가격이 메뉴 상품 목록 가격의 합보다 클 수 없습니다.");
    }

    private void 메뉴_목록_조회_성공(List<MenuResponse> 조회된_메뉴_응답_목록, List<Menu> 조회할_메뉴_목록) {
        assertThat(조회된_메뉴_응답_목록)
                .hasSize(조회할_메뉴_목록.size());
        assertThat(조회된_메뉴_응답_목록.get(0).getId())
                .isEqualTo(조회할_메뉴_목록.get(0).getId());
        assertThat(조회된_메뉴_응답_목록.get(0).getName())
                .isEqualTo(조회할_메뉴_목록.get(0).getName());
        assertThat(조회된_메뉴_응답_목록.get(0).getPrice())
                .isEqualTo(조회할_메뉴_목록.get(0).getPrice());
    }
}
