package kitchenpos.menu;

import kitchenpos.AcceptanceTest;
import kitchenpos.application.MenuService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("메뉴 관련 기능")
class MenuServiceTest extends AcceptanceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Test
    @DisplayName("메뉴 그룹이 존재하지 않을 경우 예외가 발생한다.")
    void nonExistMenuGroup() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            menuService.create(new MenuCreateRequest(1L));
        });
    }

    @Test
    @DisplayName("메뉴에 등록하고자 하는 상품이 존재하지 않을 경우 예외가 발생한다.")
    void nonExistProduct() {
        // given
        final MenuGroup savedMenuGroup = menuGroupDao.save(MenuGroup.builder().name("추천메뉴").build());

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> {
            menuService.create(new MenuCreateRequest("후라이드+후라이드", BigDecimal.valueOf(18000), savedMenuGroup.getId(), Arrays.asList(new MenuCreateRequest.MenuProduct(1L, 1L))));
        });
    }
}
