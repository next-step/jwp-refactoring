package kitchenpos.Integration;

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
public class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    @DisplayName("메뉴 관리")
    public void menuManage() {
        // given
        // 메뉴 생성
        MenuProductRequest 간장치킨_세트 = new MenuProductRequest(5L, 1L);
        MenuProductRequest 순살치킨_세트 = new MenuProductRequest(6L, 1L);
        MenuRequest menuRequest = new MenuRequest(
                "간장 순살 세트"
                , BigDecimal.valueOf(32_000)
                , 1L
                , Arrays.asList(간장치킨_세트, 순살치킨_세트));

        // when
        // 메뉴 등록
        MenuResponse menuResponse = menuService.create(menuRequest);
        // then
        // 메뉴 등록됨
        assertThat(menuResponse.getId()).isNotNull();
        assertThat(menuResponse.getMenuProductResponses()).hasSize(2);

        // when
        // 메뉴 리스트 조회
        List<MenuResponse> savedMenus = menuService.list();

        // then
        // 메뉴 리스트 조회됨
        assertThat(savedMenus).hasSize(7);
        assertThat(savedMenus).contains(menuResponse);
    }
}
