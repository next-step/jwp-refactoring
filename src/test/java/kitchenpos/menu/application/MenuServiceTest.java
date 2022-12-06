package kitchenpos.menu.application;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.MenuExceptionCode;
import kitchenpos.menu.exception.MenuGroupExceptionCode;
import kitchenpos.menu.exception.ProductExceptionCode;
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
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("MenuService 테스트")
@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    private static final Long PRODUCT_ID_1 = 1L;
    private static final Long PRODUCT_ID_2 = 2L;
    private static final Long PRODUCT_ID_3 = 3L;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MenuService menuService;

    private MenuGroup 양식;
    private Menu 양식_세트;
    private Product 스파게티;
    private Product 스테이크;
    private Product 에이드;
    private HashMap<Long, Long> quantityOfProducts;

    @BeforeEach
    void setUp() {
        양식 = new MenuGroup("양식");
        양식_세트 = new Menu("양식 세트", new BigDecimal(50000), 양식);

        스테이크 = new Product("스테이크", new BigDecimal(25000));
        스파게티 = new Product("스파게티", new BigDecimal(18000));
        에이드 = new Product("에이드", new BigDecimal(3500));

        양식_세트.addMenuProduct(new MenuProduct(양식_세트, 스테이크, 1L));
        양식_세트.addMenuProduct(new MenuProduct(양식_세트, 스파게티, 1L));
        양식_세트.addMenuProduct(new MenuProduct(양식_세트, 에이드, 2L));

        quantityOfProducts = new HashMap<>();
        quantityOfProducts.put(PRODUCT_ID_1, 1L);
        quantityOfProducts.put(PRODUCT_ID_2, 1L);
        quantityOfProducts.put(PRODUCT_ID_3, 2L);

        ReflectionTestUtils.setField(양식, "id", 1L);
        ReflectionTestUtils.setField(양식_세트, "id", 1L);
        ReflectionTestUtils.setField(스테이크, "id", PRODUCT_ID_1);
        ReflectionTestUtils.setField(스파게티, "id", PRODUCT_ID_2);
        ReflectionTestUtils.setField(에이드, "id", PRODUCT_ID_3);
    }

    @Test
    void 메뉴_그룹이_등록되어_있지_않으면_메뉴를_등록할_수_없음() {
        MenuRequest request = new MenuRequest("양식 세트", new BigDecimal(50000), 양식.getId(), quantityOfProducts);

        given(menuGroupRepository.findById(양식.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            menuService.create(request);
        }).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(MenuGroupExceptionCode.NOT_FOUND_BY_ID.getMessage());
    }

    @Test
    void 메뉴에_포함된_상품_중_등록되지_않은_상품이_있으면_메뉴를_등록할_수_없음() {
        MenuRequest request = new MenuRequest("양식 세트", new BigDecimal(50000), 양식.getId(), quantityOfProducts);

        given(menuGroupRepository.findById(양식.getId())).willReturn(Optional.of(양식));
        given(productRepository.findAllById(quantityOfProducts.keySet()))
                .willReturn(Arrays.asList(스테이크));

        assertThatThrownBy(() -> {
            menuService.create(request);
        }).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(ProductExceptionCode.NOT_FOUND_BY_ID.getMessage());
    }

    @Test
    void 메뉴에_포함된_상품의_총_금액보다_메뉴의_가격이_크면_메뉴를_등록할_수_없음() {
        MenuRequest request = new MenuRequest("양식 세트", new BigDecimal(55000), 양식.getId(), quantityOfProducts);

        given(menuGroupRepository.findById(양식.getId())).willReturn(Optional.of(양식));
        given(productRepository.findAllById(quantityOfProducts.keySet()))
                .willReturn(Arrays.asList(스테이크, 스파게티, 에이드));

        assertThatThrownBy(() -> {
            menuService.create(request);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MenuExceptionCode.INVALID_PRICE.getMessage());
    }

    @Test
    void 메뉴_등록() {
        MenuRequest request = new MenuRequest("양식 세트", new BigDecimal(50000), 양식.getId(), quantityOfProducts);

        given(menuGroupRepository.findById(양식.getId())).willReturn(Optional.of(양식));
        given(productRepository.findAllById(quantityOfProducts.keySet()))
                .willReturn(Arrays.asList(스테이크, 스파게티, 에이드));
        given(menuRepository.save(any(Menu.class))).willReturn(양식_세트);

        MenuResponse menuResponse = menuService.create(request);

        assertThat(menuResponse).satisfies(res -> {
            assertThat(res.getId()).isNotNull();
            assertEquals("양식 세트", res.getName());
            assertEquals(new BigDecimal(50000), res.getPrice());
            assertEquals(양식.getId(), res.getMenuGroupId());
            assertEquals(3, res.getMenuProducts().size());
        });
    }

    @Test
    void 메뉴_목록_조회() {
        given(menuRepository.findAll()).willReturn(Arrays.asList(양식_세트));

        List<MenuResponse> responses = menuService.list();

        assertThat(responses).hasSize(1);
    }
}
