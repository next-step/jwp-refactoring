package kitchenpos.product.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGeneratedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class MenuEventHandlerTest {

    @Mock
    private ProductService productService;

    private MenuEventHandler menuEventHandler;

    @BeforeEach
    void setUp() {
        menuEventHandler = new MenuEventHandler(productService);
    }

    @Test
    @DisplayName("상품이 포함되지 않은 메뉴 저장시 예외를 던진다")
    void create_menu_with_no_products() {
        Menu menu = Menu.of("반반치킨", BigDecimal.valueOf(16000), null);
        MenuGeneratedEvent menuGeneratedEvent = new MenuGeneratedEvent(menu);

        assertThatThrownBy(() -> menuEventHandler.createMenuEventListener(menuGeneratedEvent))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
