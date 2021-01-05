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
import org.junit.jupiter.api.condition.DisabledOnOs;
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

    private Product product1 = new Product();
    private Product product2 = new Product();
    private MenuProduct menuProduct1 = new MenuProduct();
    private MenuProduct menuProduct2 = new MenuProduct();


    @BeforeEach
    void setup() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);

        product1.setPrice(BigDecimal.valueOf(100));
        product1.setId(1L);
        product2.setPrice(BigDecimal.valueOf(100));
        product2.setId(2L);

        menuProduct1.setProductId(product1.getId());
        menuProduct1.setQuantity(1);
        menuProduct2.setProductId(product2.getId());
        menuProduct2.setQuantity(1);
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

    @DisplayName("존재하지 않는 메뉴 그룹으로 메뉴를 등록할 수 없다.")
    @Test
    void createFailWithNotExistMenuGroupTest() {
        // given
        Long notExistMenuGroupId = 4L;
        Menu withNotExistMenuGroup = new Menu();
        withNotExistMenuGroup.setMenuGroupId(notExistMenuGroupId);
        withNotExistMenuGroup.setPrice(BigDecimal.ONE);

        given(menuGroupDao.existsById(notExistMenuGroupId)).willReturn(false);

        // when, then
        assertThatThrownBy(() -> menuService.create(withNotExistMenuGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 상품들 가격의 총합보다 비싸게 메뉴 가격을 정할 수 없다.")
    @Test
    void createFailWithTooExpensivePriceTest() {
        // given
        Long menuGroupId = 1L;
        Menu tooExpensiveMenu = new Menu();
        BigDecimal menuProductPriceSum = product1.getPrice().add(product2.getPrice());
        tooExpensiveMenu.setPrice(menuProductPriceSum.add(BigDecimal.ONE));
        tooExpensiveMenu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));
        tooExpensiveMenu.setMenuGroupId(menuGroupId);

        given(menuGroupDao.existsById(menuGroupId)).willReturn(true);
        given(productDao.findById(product1.getId())).willReturn(Optional.of(product1));
        given(productDao.findById(product2.getId())).willReturn(Optional.of(product2));

        // when, then
        assertThatThrownBy(() -> menuService.create(tooExpensiveMenu)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 상품으로 구성된 메뉴 상품으로 메뉴를 만들 수 없다.")
    @Test
    void createFailWithNotExistProduct() {
        // given
        Long menuGroupId = 1L;
        Menu menuWithNotExistProduct = new Menu();
        menuWithNotExistProduct.setPrice(BigDecimal.ONE);
        menuWithNotExistProduct.setMenuGroupId(menuGroupId);
        menuWithNotExistProduct.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));

        given(menuGroupDao.existsById(menuGroupId)).willReturn(true);
        given(productDao.findById(product1.getId())).willThrow(new IllegalArgumentException());

        // when, then
        assertThatThrownBy(() -> menuService.create(menuWithNotExistProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
