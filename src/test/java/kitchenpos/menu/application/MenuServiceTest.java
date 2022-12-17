package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductTestFixture;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuTestFixture;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.validator.MenuValidator;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupTestFixture;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductTestFixture;
import kitchenpos.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuValidator menuValidator;

    @InjectMocks
    private MenuService menuService;

    private Product 하와이안피자;
    private Product 콜라;
    private Product 피클;
    private MenuGroup 피자;
    private Menu 하와이안피자세트;
    private MenuProduct 하와이안피자상품;
    private MenuProduct 콜라상품;
    private MenuProduct 피클상품;
    private List<MenuProductRequest> 상품요청 = new ArrayList<>();


    @BeforeEach
    void setUp() {
        하와이안피자 = ProductTestFixture.create(1L, "하와이안피자", BigDecimal.valueOf(15_000));
        콜라 = ProductTestFixture.create(2L, "콜라", BigDecimal.valueOf(2_000));
        피클 = ProductTestFixture.create(3L, "피클", BigDecimal.valueOf(1_000));

        피자 = MenuGroupTestFixture.create(1L, "피자");

        하와이안피자상품 = MenuProductTestFixture.create(1L, null, 하와이안피자, 1L);
        콜라상품 = MenuProductTestFixture.create(2L, null, 콜라, 1L);
        피클상품 = MenuProductTestFixture.create(3L, null, 피클, 1L);

        하와이안피자세트 = MenuTestFixture.create(1L, "하와이안피자세트", BigDecimal.valueOf(18_000L), 피자, Arrays.asList(하와이안피자상품, 콜라상품, 피클상품));

        상품요청.add(MenuProductRequest.of(하와이안피자상품.getSeq(), 1L));
        상품요청.add(MenuProductRequest.of(콜라상품.getSeq(), 1L));
        상품요청.add(MenuProductRequest.of(피클상품.getSeq(), 1L));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        MenuRequest menuRequest = MenuRequest.of(하와이안피자세트.getName().value(), 하와이안피자세트.getPrice().value(), 피자.getId(), 상품요청);
        when(menuRepository.save(menuRequest.toMenu())).thenReturn(하와이안피자세트);

        MenuResponse result = menuService.create(menuRequest);

        assertAll(
            () -> assertThat(result.getId()).isEqualTo(하와이안피자세트.getId()),
            () -> assertThat(result.getName()).isEqualTo(하와이안피자세트.getName().value())
        );
    }

    @DisplayName("메뉴 가격이 null이면 예외가 발생한다.")
    @Test
    void createMenuNullPriceException() {
        MenuRequest menuRequest = MenuRequest.of(하와이안피자세트.getName().value(), null, 피자.getId(), 상품요청);

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 0원보다 작으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -1000, -10000})
    void crateMenuUnderZeroPriceException(int price) {
        MenuRequest menuRequest = MenuRequest.of(하와이안피자세트.getName().value(), BigDecimal.valueOf(price), 피자.getId(), 상품요청);

        assertThatThrownBy(() -> menuService.create(menuRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 조회할 수 있다.")
    @Test
    void findAllMenu() {
        List<Menu> menus = Arrays.asList(하와이안피자세트);
        when(menuRepository.findAll()).thenReturn(menus);

        List<MenuResponse> result = menuService.list();

        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result.stream().map(MenuResponse::getName))
                .containsExactly(하와이안피자세트.getName().value())
        );
    }
}
