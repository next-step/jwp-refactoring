package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuGroupService menuGroupService;

    private MenuGroup 한식;
    private MenuGroup 중식;
    private MenuGroup 양식;

    @BeforeEach
    void before() {

        //given
        한식 = menuGroupRepository.save(MenuGroupFixtureFactory.create("한식"));
        중식 = menuGroupRepository.save(MenuGroupFixtureFactory.create("중식"));
        양식 = menuGroupRepository.save(MenuGroupFixtureFactory.create("양식"));
    }

    @DisplayName("메뉴 그룹을 생성 할 수 있다.")
    @Test
    void createTest() {

        //given
        MenuGroupRequest 저장할_메뉴그룹 = MenuGroupRequest.from("메뉴그룹1");

        //when
        MenuGroupResponse menuGroup = menuGroupService.create(저장할_메뉴그룹);

        //then
        assertThat(menuGroup).isNotNull();
    }

    @DisplayName("메뉴 그릅의 목록을 조회 할 수 있다.")
    @Test
    void listTest() {

        //when
        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        //then
        assertThat(menuGroups).containsExactly(
                MenuGroupResponse.from(한식),
                MenuGroupResponse.from(중식),
                MenuGroupResponse.from(양식)
        );
    }
}
