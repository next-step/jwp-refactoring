package kitchenpos.menu.application;

import static kitchenpos.helper.MenuGroupFixtures.인기메뉴_요청;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@DisplayName("메뉴 그룹 관련 Service 기능 테스트")
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Order(1)
    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        //when
        MenuGroupResponse result = menuGroupService.create(인기메뉴_요청);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(인기메뉴_요청.getName());
    }

    @Order(2)
    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        //when
        List<MenuGroupResponse> results = menuGroupService.list();

        //then
        assertThat(results.stream().map(MenuGroupResponse::getName).collect(Collectors.toList()))
                .contains(인기메뉴_요청.getName());
    }

}
