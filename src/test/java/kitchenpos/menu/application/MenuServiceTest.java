package kitchenpos.menu.application;

import kitchenpos.util.TestSupport;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 서비스 통합 테스트")
@Transactional
@SpringBootTest
public class MenuServiceTest extends TestSupport {
    @Autowired
    private MenuService menuService;

    private Product 후라이드;
    private Product 양념치킨;
    private MenuGroup 치킨;

    @BeforeEach
    public void setUp() {
        후라이드 = 상품_등록되어있음("후라이드", BigDecimal.valueOf(18_000));
        양념치킨 = 상품_등록되어있음("후라이드", BigDecimal.valueOf(19_000));
        치킨 = 메뉴그룹_등록되어있음("치킨");
    }

    @DisplayName("메뉴 등록 예외 - 메뉴그룹이 존재하지 않는 경우")
    @Test
    public void 메뉴그룹이없는경우_메뉴등록_예외() throws Exception {
        //given
        MenuProductRequest 치킨후라이드 = new MenuProductRequest(후라이드.getId(), 1L);
        MenuProductRequest 치킨양념치킨 = new MenuProductRequest(양념치킨.getId(), 1L);
        MenuRequest menuRequest = new MenuRequest("원플러스원치킨", BigDecimal.valueOf(18_000), 100L,
                Arrays.asList(치킨후라이드, 치킨양념치킨));

        //when
        //then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .hasMessage("메뉴그룹이 존재하지 않습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 예외 - 존재하지않는 상품으로 메뉴등록할 경우")
    @Test
    public void 존재하지않는상품으로_메뉴등록_예외() throws Exception {
        //given
        MenuProductRequest 치킨후라이드 = new MenuProductRequest(100L, 1L);
        MenuProductRequest 치킨양념치킨 = new MenuProductRequest(양념치킨.getId(), 1L);
        MenuRequest menuRequest = new MenuRequest("원플러스원치킨", BigDecimal.valueOf(18_000), 치킨.getId(),
                Arrays.asList(치킨후라이드, 치킨양념치킨));

        //when
        //then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .hasMessage("상품이 존재하지 않습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 예외 - 입력받은 메뉴가격이 상품의 총 가격보다 더 큰 경우")
    @Test
    public void 입력받은메뉴가격이상품총가격보다큰경우_메뉴등록_예외() throws Exception {
        //given
        MenuProductRequest 치킨후라이드 = new MenuProductRequest(후라이드.getId(), 1L);
        MenuProductRequest 치킨양념치킨 = new MenuProductRequest(양념치킨.getId(), 1L);
        MenuRequest menuRequest = new MenuRequest("원플러스원치킨", BigDecimal.valueOf(900_000), 치킨.getId(),
                Arrays.asList(치킨후라이드, 치킨양념치킨));

        //when
        //then
        assertThatThrownBy(() -> menuService.create(menuRequest))
                .hasMessage("입력받은 메뉴가격이 상품의 총 가격보다 같거나 작아야합니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록")
    @Test
    public void 메뉴_등록_확인() throws Exception {
        //given
        MenuProductRequest 치킨후라이드 = new MenuProductRequest(후라이드.getId(), 1L);
        MenuProductRequest 치킨양념치킨 = new MenuProductRequest(양념치킨.getId(), 1L);
        MenuRequest menuRequest = new MenuRequest("원플러스원치킨", BigDecimal.valueOf(18_000), 치킨.getId(),
                Arrays.asList(치킨후라이드, 치킨양념치킨));

        //when
        MenuResponse menuResponse = menuService.create(menuRequest);

        //then
        assertThat(menuResponse.getId()).isNotNull();
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    public void 메뉴_목록조회_확인() throws Exception {
        //given
        MenuProductRequest 치킨후라이드 = new MenuProductRequest(후라이드.getId(), 1L);
        MenuProductRequest 치킨양념치킨 = new MenuProductRequest(양념치킨.getId(), 1L);
        MenuRequest menuRequest1 = new MenuRequest("원플러스원치킨1", BigDecimal.valueOf(18_000), 치킨.getId(),
                Arrays.asList(치킨후라이드, 치킨양념치킨));
        MenuRequest menuRequest2 = new MenuRequest("원플러스원치킨2", BigDecimal.valueOf(18_000), 치킨.getId(),
                Arrays.asList(치킨후라이드, 치킨양념치킨));
        MenuRequest menuRequest3 = new MenuRequest("원플러스원치킨3", BigDecimal.valueOf(18_000), 치킨.getId(),
                Arrays.asList(치킨후라이드, 치킨양념치킨));
        menuService.create(menuRequest1);
        menuService.create(menuRequest2);
        menuService.create(menuRequest3);

        //when
        List<MenuResponse> menus = menuService.list();

        //then
        assertThat(menus.size()).isEqualTo(3);
    }
}
