package kitchenpos.application;

import static common.MenuProductFixture.*;
import static common.ProductFixture.양념치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import common.MenuFixture;
import common.MenuProductFixture;
import common.ProductFixture;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

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
    void 메뉴그룹이_존재여부_확인() {
        // given
        Menu 메뉴_양념치킨 = MenuFixture.메뉴_양념치킨();

        when(menuGroupDao.existsById(메뉴_양념치킨.getId())).thenReturn(false);

        // then
        assertThatThrownBy(() -> {
            menuService.create(메뉴_양념치킨);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_저장() {
        // given
        Menu 메뉴_양념치킨 = MenuFixture.메뉴_양념치킨();
        Product 양념치킨 = 양념치킨();
        MenuProduct 양념치킨_1개 = 양념치킨_1개();

        // mocking
        when(menuGroupDao.existsById(메뉴_양념치킨.getId())).thenReturn(true);
        when(productDao.findById(any())).thenReturn(Optional.of(양념치킨));
        when(menuDao.save(메뉴_양념치킨)).thenReturn(메뉴_양념치킨);
        when(menuProductDao.save(any(MenuProduct.class))).thenReturn(양념치킨_1개);

        // when
        Menu 저장된_메뉴_양념치킨 = menuService.create(메뉴_양념치킨);

        // then
        assertThat(저장된_메뉴_양념치킨).isEqualTo(메뉴_양념치킨);
        assertThat(저장된_메뉴_양념치킨.getMenuProducts()).containsExactly(양념치킨_1개);
    }

    @Test
    void 메뉴_조회() {
        // given
        Menu 메뉴_양념치킨 = MenuFixture.메뉴_양념치킨();
        Menu 메뉴_후라이드 = MenuFixture.메뉴_후라이드();

        // mocking
        when(menuDao.findAll()).thenReturn(Arrays.asList(메뉴_양념치킨, 메뉴_후라이드));
        when(menuProductDao.findAllByMenuId(any(Long.class))).thenReturn(Arrays.asList(양념치킨_1개(), 후라이드_1개()));

        // when
        List<Menu> list = menuService.list();

        // then
        assertThat(list).containsExactly(메뉴_양념치킨, 메뉴_후라이드);
    }

}
