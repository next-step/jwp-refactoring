package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    private Product 첫번째_상품;
    private Product 두번째_상품;
    private MenuProduct 첫번째_메뉴상품;
    private MenuProduct 두번째_메뉴상품;
    private List<MenuProduct> 메뉴상품_목록;

    @BeforeEach
    void setUp() {
        첫번째_상품 = new Product(1L, "짬뽕", BigDecimal.valueOf(9000));
        두번째_상품 = new Product(2L, "짜장면", BigDecimal.valueOf(8000));
        첫번째_메뉴상품 = new MenuProduct(1L, 2);
        두번째_메뉴상품 = new MenuProduct(2L, 1);
        메뉴상품_목록 = Arrays.asList(첫번째_메뉴상품, 두번째_메뉴상품);
    }

    @DisplayName("메뉴를 생성할 수 있다.")
    @Test
    void createMenu() {
        // given
        Menu 생성할_메뉴 = new Menu("짬짜면세트", BigDecimal.valueOf(26000), 1L, 메뉴상품_목록);
        given(menuGroupRepository.existsById(anyLong()))
                .willReturn(true);
        given(productRepository.findById(1L))
                .willReturn(Optional.of(첫번째_상품));
        given(productRepository.findById(2L))
                .willReturn(Optional.of(두번째_상품));
        given(menuRepository.save(생성할_메뉴))
                .willReturn(new Menu(1L, "짬짜면세트", BigDecimal.valueOf(26000), 1L, 메뉴상품_목록));
        given(menuProductRepository.save(첫번째_메뉴상품))
                .willReturn(첫번째_메뉴상품);
        given(menuProductRepository.save(두번째_메뉴상품))
                .willReturn(두번째_메뉴상품);

        // when
        Menu 생성된_메뉴 = menuService.create(생성할_메뉴);

        // then
        메뉴_생성_성공(생성된_메뉴, 생성할_메뉴);
    }

    @DisplayName("메뉴 가격 정보가 없으면 메뉴 생성에 실패한다.")
    @Test
    void createMenuFailsWhenNullPrice() {
        // given
        Menu 생성할_메뉴 = new Menu("짬짜면세트", null, 1L, 메뉴상품_목록);

        // when & then
        메뉴_생성_실패(생성할_메뉴);
    }

    @DisplayName("메뉴 가격이 0 미만이면 메뉴 생성에 실패한다.")
    @Test
    void createMenuFailsWhenMinusPrice() {
        // given
        Menu 생성할_메뉴 = new Menu("짬짜면세트", BigDecimal.valueOf(-26000), 1L, 메뉴상품_목록);

        // when & then
        메뉴_생성_실패(생성할_메뉴);
    }

    @DisplayName("메뉴 그룹이 존재하지 않으면 메뉴 생성에 실패한다.")
    @Test
    void createMenuFailsWhenNoMenuGroup() {
        // given
        Long 존재하지_않는_메뉴그룹ID = 1000L;
        Menu 생성할_메뉴 = new Menu("짬짜면세트", BigDecimal.valueOf(26000), 존재하지_않는_메뉴그룹ID, 메뉴상품_목록);
        given(menuGroupRepository.existsById(anyLong()))
                .willReturn(true);

        // when & then
        메뉴_생성_실패(생성할_메뉴);
    }

    @DisplayName("메뉴의 상품이 존재하지 않으면 메뉴 생성에 실패한다.")
    @Test
    void createMenuFailsWhenNoProduct() {
        // given
        Long 존재하지_않는_상품ID = 1000L;
        MenuProduct 유효하지_않은_메뉴상품 = new MenuProduct(존재하지_않는_상품ID, 10);
        Menu 생성할_메뉴 = new Menu("짬짜면세트", BigDecimal.valueOf(26000), 1L, Arrays.asList(유효하지_않은_메뉴상품));

        // when & then
        메뉴_생성_실패(생성할_메뉴);
    }

    @DisplayName("메뉴의 가격이 메뉴에 포함된 상품들의 금액 합보다 크면 메뉴 생성에 실패한다.")
    @Test
    void createMenuFailsWhenInvalidPrice() {
        // given
        Menu 생성할_메뉴 = new Menu("짬짜면세트", BigDecimal.valueOf(27000), 1L, 메뉴상품_목록);
        given(menuGroupRepository.existsById(anyLong()))
                .willReturn(true);
        given(productRepository.findById(1L))
                .willReturn(Optional.of(첫번째_상품));
        given(productRepository.findById(2L))
                .willReturn(Optional.of(두번째_상품));

        // when & then
        메뉴_생성_실패(생성할_메뉴);
    }

    @DisplayName("메뉴 목록 조회를 할 수 있다.")
    @Test
    void listMenu() {
        // given
        Menu 생성된_메뉴 = new Menu(1L, "짬짜면세트", BigDecimal.valueOf(26000), 1L, 메뉴상품_목록);
        List<Menu> 조회할_메뉴_목록 = Arrays.asList(생성된_메뉴);
        given(menuRepository.findAll())
                .willReturn(조회할_메뉴_목록);

        // when
        List<Menu> 조회된_메뉴_목록 = menuService.list();

        // then
        메뉴_목록_조회_성공(조회된_메뉴_목록, 조회할_메뉴_목록);
    }

    private void 메뉴_생성_성공(Menu 생성된_메뉴, Menu 생성할_메뉴) {
        assertAll(
                () -> assertThat(생성된_메뉴.getId())
                        .isNotNull(),
                () -> assertThat(생성된_메뉴.getName())
                        .isEqualTo(생성할_메뉴.getName()),
                () -> assertThat(생성된_메뉴.getPrice())
                        .isEqualTo(생성할_메뉴.getPrice()),
                () -> assertThat(생성된_메뉴.getMenuGroupId())
                        .isEqualTo(생성할_메뉴.getMenuGroupId()),
                () -> assertThat(생성된_메뉴.getMenuProducts())
                        .containsExactlyElementsOf(생성할_메뉴.getMenuProducts())
        );
    }

    private void 메뉴_생성_실패(Menu 생성할_메뉴) {
        assertThatThrownBy(() -> menuService.create(생성할_메뉴))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    private void 메뉴_목록_조회_성공(List<Menu> 조회된_메뉴_목록, List<Menu> 조회할_메뉴_목록) {
        assertThat(조회된_메뉴_목록)
                .hasSize(조회할_메뉴_목록.size());
        assertThat(조회된_메뉴_목록)
                .hasSameElementsAs(조회할_메뉴_목록);
    }
}
