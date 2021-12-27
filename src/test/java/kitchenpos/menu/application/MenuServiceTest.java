package kitchenpos.menu.application;

import static common.MenuFixture.메뉴_양념치킨;
import static common.MenuFixture.메뉴_후라이드;
import static common.MenuGroupFixture.메뉴그룹_한마리;
import static common.MenuProductFixture.양념치킨_1개;
import static common.ProductFixture.양념치킨;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuValidation;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuDao menuDao;

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuValidation menuValidation;

    @Test
    void 메뉴_저장() {
        // given
        Menu menu = 메뉴_양념치킨();
        MenuProduct menuProduct1EA = 양념치킨_1개();

        MenuRequest menuRequest = new MenuRequest(
            menu.getName(),
            16000L,
            menu.getMenuGroupId(),
            asList(new MenuProductRequest(menuProduct1EA.getId(), menuProduct1EA.getQuantity())
            ));

        // mocking
        when(menuDao.save(any())).thenReturn(menu);
        doNothing().when(menuValidation).valid(any());

        // when
        menuService.create(menuRequest);

        // then
        verify(menuDao, atMost(1)).save(menu);
    }

    @Test
    void 메뉴_조회() {
        // given
        Menu 메뉴_양념치킨 = 메뉴_양념치킨();
        Menu 메뉴_후라이드 = 메뉴_후라이드();

        // mocking
        when(menuDao.findAll()).thenReturn(asList(메뉴_양념치킨, 메뉴_후라이드));

        // when
        List<MenuResponse> list = menuService.list();

        // then
        assertThat(list.size()).isEqualTo(2);
    }

}
