package kitchenpos.menu.application;

import static kitchenpos.menu.helper.MenuGroupFixtures.인기메뉴_그룹_요청;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DisplayName("메뉴 그룹 관련 Service 기능 테스트")
@DataJpaTest
@Import(MenuGroupService.class)
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        //when
        MenuGroupResponse result = menuGroupService.create(인기메뉴_그룹_요청);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(인기메뉴_그룹_요청.getName());
    }

    @DisplayName("메뉴 상품 목록을 조회한다.")
    @Test
    void list() {
        //when
        List<MenuGroupResponse> results = menuGroupService.findAllMenuGroups();

        //then
        assertThat(results.stream().map(MenuGroupResponse::getName).collect(Collectors.toList()))
                .contains("두마리메뉴", "한마리메뉴", "순살파닭두마리메뉴", "신메뉴");
    }

}
