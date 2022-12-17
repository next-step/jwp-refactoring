package kitchenpos.menu.application;

import kitchenpos.exception.MenuErrorMessage;
import kitchenpos.exception.MenuGroupErrorMessage;
import kitchenpos.exception.ProductErrorMessage;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    private MenuGroup 일식;
    private Menu 일식_세트;
    private Product 광어초밥;
    private Product 연어초밥;
    private Product 우동;
    private List<MenuProductRequest> menuProducts;

    @BeforeEach
    void setUp() {
        일식 = new MenuGroup("일식");
        일식_세트 = new Menu("일식_세트", new BigDecimal(50_000), 일식);

        연어초밥 = new Product("연어초밥", new BigDecimal(20_000));
        광어초밥 = new Product("광어초밥", new BigDecimal(20_000));
        우동 = new Product("우동", new BigDecimal(5_000));

        ReflectionTestUtils.setField(일식, "id", 1L);
        ReflectionTestUtils.setField(일식_세트, "id", 1L);

        ReflectionTestUtils.setField(연어초밥, "id", 1L);
        ReflectionTestUtils.setField(광어초밥, "id", 2L);
        ReflectionTestUtils.setField(우동, "id", 3L);

        일식_세트.create(Arrays.asList(
                new MenuProduct(일식_세트, 광어초밥, 1L),
                new MenuProduct(일식_세트, 연어초밥, 1L),
                new MenuProduct(일식_세트, 우동, 2L)));

        menuProducts = Arrays.asList(
                new MenuProductRequest(연어초밥.getId(), 1L),
                new MenuProductRequest(광어초밥.getId(), 1L),
                new MenuProductRequest(우동.getId(), 2L));
    }

    @DisplayName("메뉴의 가격은 무조건 존재해야 한다.")
    @Test
    void 메뉴의_가격은_무조건_존재해야_한다() {
        // given
        MenuRequest menuRequest = new MenuRequest(일식_세트.getName(), null, 일식.getId(), menuProducts);

        when(menuGroupRepository.findById(anyLong())).thenReturn(Optional.of(일식));
        when(productRepository.findAllById(anyList())).thenReturn(Arrays.asList(연어초밥, 광어초밥, 우동));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menuRequest))
                .withMessage(MenuErrorMessage.REQUIRED_PRICE.getMessage());
    }

    @DisplayName("상품의 가격은 0원 이상이어야 한다.")
    @Test
    void 상품의_가격은_0원_이상이어야_한다() {
        // given
        MenuRequest menuRequest = new MenuRequest(일식_세트.getName(), BigDecimal.valueOf(-1), 일식.getId(), menuProducts);

        when(menuGroupRepository.findById(anyLong())).thenReturn(Optional.of(일식));
        when(productRepository.findAllById(anyList())).thenReturn(Arrays.asList(연어초밥, 광어초밥, 우동));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menuRequest))
                .withMessage(MenuErrorMessage.INVALID_PRICE.getMessage());
    }

    @DisplayName("메뉴 그룹 아이디가 존재하지 않으면 등록할 수 없다.")
    @Test
    void 메뉴_그룹_아이디가_존재하지_않으면_등록할_수_없다() {
        // given
        when(menuGroupRepository.findById(anyLong())).thenReturn(Optional.empty());

        MenuRequest menuRequest = new MenuRequest(일식_세트.getName(), 일식_세트.getPrice(), 일식.getId(), menuProducts);

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menuRequest))
                .withMessage(MenuGroupErrorMessage.NOT_FOUND_BY_ID.getMessage());
    }

    @DisplayName("메뉴 상품은 모두 등록된 상품이어야 한다.")
    @Test
    void 메뉴_상품은_모두_등록된_상품이어야_한다() {
        // given
        when(menuGroupRepository.findById(anyLong())).thenReturn(Optional.of(일식));
        when(productRepository.findAllById(anyList())).thenReturn(Arrays.asList(연어초밥, 광어초밥));

        MenuRequest menuRequest = new MenuRequest(일식_세트.getName(), 일식_세트.getPrice(), 일식.getId(), menuProducts);

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menuRequest))
                .withMessage(ProductErrorMessage.NOT_FOUND_BY_ID.getMessage());
    }

    @DisplayName("메뉴의 가격은 메뉴 상품들의 가격의 총 가격보다 클 수 없다.")
    @Test
    void 메뉴의_가격은_메뉴_상품들의_가격의_총_가격보다_클_수_없다() {
        // given
        MenuRequest menuRequest = new MenuRequest(일식_세트.getName(), BigDecimal.valueOf(50_001), 일식.getId(), menuProducts);

        when(menuGroupRepository.findById(anyLong())).thenReturn(Optional.of(일식));
        when(productRepository.findAllById(anyList())).thenReturn(Arrays.asList(연어초밥, 광어초밥, 우동));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> menuService.create(menuRequest))
                .withMessage(MenuErrorMessage.INVALID_PRICE.getMessage());
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void 메뉴를_등록한다() {
        // given
        when(menuGroupRepository.findById(anyLong())).thenReturn(Optional.of(일식));
        when(productRepository.findAllById(anyList())).thenReturn(Arrays.asList(연어초밥, 광어초밥, 우동));
        when(menuRepository.save(any(Menu.class))).thenReturn(일식_세트);

        MenuRequest menuRequest = new MenuRequest(일식_세트.getName(), 일식_세트.getPrice(), 일식.getId(), menuProducts);

        // when
        MenuResponse menuResponse = menuService.create(menuRequest);

        // then
        assertThat(menuResponse).satisfies(response -> {
            assertThat(response.getId()).isNotNull();
            assertEquals(response.getName(), 일식_세트.getName());
            assertEquals(response.getPrice(), 일식_세트.getPrice());
            assertEquals(response.getMenuGroup().getId(), 일식.getId());
            assertEquals(response.getMenuProducts().size(), 3);
        });
    }

    @DisplayName("메뉴를 조회한다.")
    @Test
    void 메뉴를_조회한다() {
        // given
        when(menuRepository.findAll()).thenReturn(Arrays.asList(일식_세트));

        // when
        List<MenuResponse> responses = menuService.list();

        // then
        assertThat(responses).hasSize(1);
    }
}
