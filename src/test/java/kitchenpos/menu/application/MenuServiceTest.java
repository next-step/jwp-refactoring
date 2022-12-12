package kitchenpos.menu.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    private static final Long PRODUCT_ID_1 = 1L;
    private static final Long PRODUCT_ID_2 = 2L;

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private MenuService menuService;

    private Product 허니콤보;
    private Product 치즈볼;
    private MenuGroup 인기그룹;
    private Menu 허니콤보세트;
    private List<MenuProductRequest> menuProducts;

    @BeforeEach
    void setUp() {
        인기그룹 = new MenuGroup("인기그룹");
        허니콤보 = new Product("허니콤보", BigDecimal.valueOf(20000));
        치즈볼 = new Product("치즈볼", BigDecimal.valueOf(5000));
        허니콤보세트 = new Menu("허니콤보세트", BigDecimal.valueOf(22000), 인기그룹);

        ReflectionTestUtils.setField(인기그룹, "id", 1L);
        ReflectionTestUtils.setField(허니콤보, "id", PRODUCT_ID_1);
        ReflectionTestUtils.setField(치즈볼, "id", PRODUCT_ID_2);
        ReflectionTestUtils.setField(허니콤보세트, "id", 1L);

        menuProducts = Arrays.asList(new MenuProductRequest(허니콤보.getId(), 1),
                new MenuProductRequest(치즈볼.getId(), 1));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void 메뉴_생성() {
        MenuRequest request = new MenuRequest("허니콤보세트", new BigDecimal(22000), 인기그룹.getId(), menuProducts);

        // given
        when(menuGroupRepository.findById(인기그룹.getId())).thenReturn(Optional.of(인기그룹));
        when(productRepository.findAllById(request.getMenuProductIds()))
                .thenReturn(Arrays.asList(허니콤보, 치즈볼));
        when(menuRepository.save(any(Menu.class))).thenReturn(허니콤보세트);

        // when
        MenuResponse menuResponse = menuService.create(request);

        // then
        assertAll(
                () -> assertThat(menuResponse.getId()).isNotNull(),
                () -> assertThat(menuResponse.getName()).isEqualTo("허니콤보세트"),
                () -> assertThat(menuResponse.getPrice()).isEqualTo(new BigDecimal(22000)),
                () -> assertThat(menuResponse.getId()).isEqualTo(허니콤보세트.getId())
        );
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void 메뉴_목록_조회() {
        // given
        when(menuRepository.findAll()).thenReturn(Collections.singletonList(허니콤보세트));

        // when
        List<MenuResponse> responses = menuService.list();

        // then
        assertThat(responses).hasSize(1);
    }
}
