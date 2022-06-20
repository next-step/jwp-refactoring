package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
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
        //given
        MenuGroup request = new MenuGroup(null, "인기 메뉴");

        //when
        MenuGroup result = menuGroupService.create(request);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(request.getName());
    }

    @Order(2)
    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        //when
        List<MenuGroup> results = menuGroupService.list();

        //then
        assertThat(results.stream().map(MenuGroup::getName).collect(Collectors.toList()))
                .contains("인기 메뉴");
    }

}
