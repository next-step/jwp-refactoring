package kitchenpos.menu.domian;

import kitchenpos.MenuApplication;
import kitchenpos.ProductApplication;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.utils.DatabaseCleanup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {MenuApplication.class, ProductApplication.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DisplayName("메뉴 관련 도메인 테스트")
public class MenuTest {
    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private MenuRepository menuRepository;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();
    }

    @DisplayName("메뉴 생성 테스트")
    @Test
    void createMenu() {
        // given
        Product 후라이드 = new Product("후라이드", BigDecimal.valueOf(18_000));
        MenuGroup 한마리치킨 = new MenuGroup("한마리치킨");

        // when
        MenuProduct menuProduct = new MenuProduct(후라이드, 3L);
        Menu 후라이드한마리치킨 = new Menu("후라이드한마리치킨", BigDecimal.valueOf(18_000 * 3L), 한마리치킨);
        Menu savedMenu = menuRepository.save(후라이드한마리치킨);
        savedMenu.addMenuProducts(Lists.newArrayList(menuProduct));

        // then
        assertThat(savedMenu).isNotNull();
        assertThat(savedMenu.getMenuProducts()).hasSize(1);
    }

    @DisplayName("메뉴 생성 테스트 / 가격 오류 Exception")
    @Test
    void createMenuException() {
        // given
        Product 후라이드 = new Product("후라이드", BigDecimal.valueOf(18_000));
        MenuGroup 한마리치킨 = new MenuGroup("한마리치킨");

        // when
        MenuProduct menuProduct = new MenuProduct(후라이드, 3L);
        Menu savedMenu = menuRepository.save(new Menu("후라이드한마리치킨", BigDecimal.valueOf(18_000 * 4L), 한마리치킨));

        // then
        assertThatThrownBy(() -> savedMenu.addMenuProducts(Lists.newArrayList(menuProduct)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
