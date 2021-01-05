package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    private MenuService menuService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @BeforeEach
    void setup() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @DisplayName("올바르지 않은 메뉴 가격으로 메뉴를 등록할 수 없다.")
    @ParameterizedTest
    @NullSource
    @MethodSource("menuCreateFailByInvalidPriceResource")
    void menuCreateFailByInvalidPrice(BigDecimal invalidPrice) {
        // given
        Menu menu = new Menu();
        menu.setPrice(invalidPrice);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
    }
    public static Stream<Arguments> menuCreateFailByInvalidPriceResource() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(-1)),
                Arguments.of(BigDecimal.valueOf(-2))
        );
    }

    @DisplayName("메뉴 상품들 가격의 총합보다 비싸게 메뉴 가격을 정할 수 없다.")
    @Test
    void createFailWithTooExpensivePriceTest() {
        // given
        Long product1Id = 1L;
        Long product2Id = 2L;
        Product product1 = new Product();
        product1.setPrice(BigDecimal.valueOf(100));
        Product product2 = new Product();
        product2.setPrice(BigDecimal.valueOf(100));

        MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setProductId(product1Id);
        menuProduct1.setQuantity(1);
        MenuProduct menuProduct2 = new MenuProduct();
        menuProduct2.setProductId(product2Id);
        menuProduct1.setQuantity(1);

        Menu tooExpensiveMenu = new Menu();
        BigDecimal menuProductPriceSum = product1.getPrice().add(product2.getPrice());
        tooExpensiveMenu.setPrice(menuProductPriceSum.add(BigDecimal.ONE));
        tooExpensiveMenu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));
        Long menuGroupId = 1L;
        tooExpensiveMenu.setMenuGroupId(menuGroupId);

        given(menuGroupDao.existsById(menuGroupId)).willReturn(true);
        given(productDao.findById(product1Id)).willReturn(Optional.of(product1));
        given(productDao.findById(product2Id)).willReturn(Optional.of(product2));

        // when, then
        assertThatThrownBy(() -> menuService.create(tooExpensiveMenu)).isInstanceOf(IllegalArgumentException.class);
    }
}
