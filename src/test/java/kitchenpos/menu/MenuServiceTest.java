package kitchenpos.menu;

import static kitchenpos.menu.MenuFixture.더블강정치킨;
import static kitchenpos.menu.MenuGroupFixture.추천메뉴;
import static kitchenpos.product.ProductFixture.강정치킨;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
        when(menuGroupDao.findById(any()))
            .thenReturn(Optional.of(추천메뉴));
        when(productDao.findById(any()))
            .thenReturn(Optional.of(강정치킨));
        when(menuDao.save(any()))
            .thenReturn(더블강정치킨);

        //when
        MenuResponse menuResponse = menuService.create(from(더블강정치킨));

        //then
        Assertions.assertThat(menuResponse).isEqualTo(MenuResponse.from(더블강정치킨));
    }

    private MenuRequest from(Menu menu) {
        return new MenuRequest(menu.getName(), menu.getPrice(), menu.menuGroupId(),
            from(menu.getMenuProducts()));
    }

    private List<MenuProductRequest> from(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(menuProduct -> new MenuProductRequest(menuProduct.getProduct().getId(),
                menuProduct.getQuantity()))
            .collect(Collectors.toList());
    }
}