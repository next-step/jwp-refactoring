package kitchenpos.menu.integrate;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("메뉴 통합 테스트")
public class MenuIntegratedTest {

    @Autowired
    private MenuService menuService;

    @Test
    @DisplayName("메뉴 관리")
    public void menuManage() {
        // given
        // 메뉴 생성
        MenuProductRequest 간장치킨_세트 = 메뉴_상품_요청(5L, 1L);
        MenuProductRequest 순살치킨_세트 = 메뉴_상품_요청(6L, 1L);
        MenuRequest menuRequest = 메뉴_요청(
                "간장 순살 세트"
                , BigDecimal.valueOf(32_000)
                , 1L
                , Arrays.asList(간장치킨_세트, 순살치킨_세트));

        // when
        // 메뉴를 등록한다.
        MenuResponse menuResponse = menuService.create(menuRequest);
        // then
        // 메뉴가 정상적으로 등록된다.
        assertThat(menuResponse.getId()).isNotNull();
        assertThat(menuResponse.getMenuProductResponses()).hasSize(2);

        // when
        // 메뉴 리스트를 조회한다.
        List<MenuResponse> savedMenus = menuService.list();

        // then
        // 메뉴 리스트가 정상적으로 조회된다.
        assertThat(savedMenus).hasSize(7);
        assertThat(savedMenus).contains(menuResponse);
    }

    private MenuProductRequest 메뉴_상품_요청(Long productId, Long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    private MenuRequest 메뉴_요청(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> productRequests) {
        return new MenuRequest(name, price, menuGroupId, productRequests);
    }
}
