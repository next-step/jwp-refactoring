package kitchenpos.menu;

import kitchenpos.application.MenuService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴")
class MenuTest {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuProductDao menuProductDao;

    @Mock
    private ProductDao productDao;

    @Test
    @DisplayName("메뉴의 이름이 비어있을 경우 예외가 발생한다.")
    void emptyMenuName() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            menuService.create(new Menu(" "));
        });
    }

    @Test
    @DisplayName("메뉴의 가격이 0원 이상이 아닐 경우 예외가 발생한다.")
    void menuPriceLessThanZero() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            menuService.create(new Menu("후라이드+후라이드", BigDecimal.valueOf(-1000)));
        });
    }
}
