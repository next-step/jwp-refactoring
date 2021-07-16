package kitchenpos.menugroup.service;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.service.MenuGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MenuGroupServiceTest {

    @Autowired
    MenuGroupService menuGroupService;

    MenuGroup 메뉴그룹_한마리메뉴;
    MenuGroup 메뉴그룹_두마리메뉴;

    MenuGroupRequest 메뉴그룹_한마리메뉴_리퀘스트;
    MenuGroupRequest 메뉴그룹_두마리메뉴_리퀘스트;

    @BeforeEach
    void setUp() {
        메뉴그룹_한마리메뉴 = new MenuGroup("한마리메뉴");
        메뉴그룹_두마리메뉴 = new MenuGroup("두마리메뉴");

        메뉴그룹_한마리메뉴_리퀘스트 = new MenuGroupRequest("한마리메뉴");
        메뉴그룹_두마리메뉴_리퀘스트 = new MenuGroupRequest("두마리메뉴");
    }

    @Test
    @DisplayName("메뉴그룹을 생성한다.")
    void create() {
        //when
        MenuGroupResponse createdMenuGroup = menuGroupService.create(메뉴그룹_한마리메뉴_리퀘스트);
        //then
        assertThat(createdMenuGroup.getName()).isEqualTo(메뉴그룹_한마리메뉴.getName());
    }

    @Test
    @DisplayName("전체 메뉴그룹을 조회한다.")
    void list() {
        //when
        List<MenuGroupResponse> foundMenuGroups = menuGroupService.list();
        //then
        assertThat(foundMenuGroups.size()).isGreaterThan(0);
    }
}
