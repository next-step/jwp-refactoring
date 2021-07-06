package kitchenpos.application.integration;

import kitchenpos.application.MenuGroupService;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴그룹 서비스 통합 테스트")
@Transactional
@SpringBootTest
public class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴그룹 등록")
    @Test
    public void 메뉴그룹_등록_확인() throws Exception {
        //given
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("치킨");

        //when
        MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupRequest);

        //then
        assertThat(menuGroupResponse.getId()).isNotNull();
    }

    @DisplayName("메뉴그룹 목록 조회")
    @Test
    public void 메느그룹목록_조회_확인() throws Exception {
        //given
        menuGroupService.create(new MenuGroupRequest("치킨1"));
        menuGroupService.create(new MenuGroupRequest("치킨2"));
        menuGroupService.create(new MenuGroupRequest("치킨3"));

        //when
        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        //then
        assertThat(menuGroups.size()).isEqualTo(3);
    }
}
