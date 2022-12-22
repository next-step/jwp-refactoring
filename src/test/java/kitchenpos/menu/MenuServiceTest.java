package kitchenpos.menu;

import static kitchenpos.menu.MenuFixture.더블강정치킨;
import static kitchenpos.menu.MenuFixture.더블강정치킨상품;
import static kitchenpos.menu.MenuFixture.더블개손해치킨상품;
import static kitchenpos.menu.MenuGroupFixture.추천메뉴;
import static kitchenpos.product.ProductFixture.강정치킨;
import static kitchenpos.product.ProductFixture.개손해치킨;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dao.ProductDao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuDao menuDao;
    @Mock
    private MenuGroupDao menuGroupDao;
    @Mock
    private MenuProductDao menuProductDao;
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("메뉴 생성")
    void createMenu() {
        //given
        when(menuGroupDao.existsById(any()))
            .thenReturn(true);
        when(productDao.findById(any()))
            .thenReturn(Optional.of(강정치킨));
        when(menuDao.save(any()))
            .thenReturn(더블강정치킨);

        //when
        MenuResponse menuResponse = menuService.create(from(더블강정치킨));

        //then
        Assertions.assertThat(menuResponse).isEqualTo(MenuResponse.from(더블강정치킨));
    }

    @Test
    @DisplayName("메뉴의 가격이 상품들의 가격 합보다 크면 안된다")
    void menuSumGreaterThanProductsSum() {
        //given
        when(menuGroupDao.existsById(any()))
            .thenReturn(true);
        when(productDao.findById(eq(1L)))
            .thenReturn(Optional.of(강정치킨));
        when(productDao.findById(eq(2L)))
            .thenReturn(Optional.of(개손해치킨));
        Menu menu = new Menu(1L, "더블강정치킨", new BigDecimal(199_000), 추천메뉴.getId(),
            Arrays.asList(더블강정치킨상품, 더블개손해치킨상품));

        //when
        Assertions.assertThatThrownBy(() -> menuService.create(from(menu)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private MenuRequest from(Menu menu) {
        return new MenuRequest(menu.getName(), menu.getPrice(), menu.getMenuGroupId(),
            from(menu.getMenuProducts()));
    }

    private List<MenuProductRequest> from(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(menuProduct -> new MenuProductRequest(menuProduct.getProduct().getId(),
                menuProduct.getQuantity()))
            .collect(Collectors.toList());
    }
}