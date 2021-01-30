package api.kitchenpos.menu.application.menu;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import api.kitchenpos.menu.dto.menu.MenuGroupRequest;
import api.kitchenpos.menu.dto.menu.MenuGroupResponse;
import domain.kitchenpos.menu.menu.MenuGroup;
import domain.kitchenpos.menu.menu.MenuGroupRepository;

@DisplayName("애플리케이션 테스트 보호 - 메뉴 그룹 서비스")
@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    private MenuGroup 메뉴그룹;
    private MenuGroupRequest 메뉴그룹_요청;

    @BeforeEach
    public void setup() {
        메뉴그룹_요청 = new MenuGroupRequest("후리이드치킨반마리+양념치킨반마리세트");
        메뉴그룹 = 메뉴그룹_요청.toMenuGroup();
    }

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {
        given(menuGroupRepository.save(메뉴그룹)).willReturn(메뉴그룹);

        MenuGroupResponse 메뉴그룹_응답 = menuGroupService.create(메뉴그룹_요청);

        assertThat(메뉴그룹_응답).isNotNull();
        assertThat(메뉴그룹_응답.getName()).isEqualTo(메뉴그룹.getName());
    }

    @DisplayName("메뉴 그룹 목록 조회")
    @Test
    void list() {
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(메뉴그룹));

        List<MenuGroupResponse> 메뉴그룹_응답_목록 = menuGroupService.findAll();

        assertThat(메뉴그룹_응답_목록).isNotEmpty();
        assertThat(메뉴그룹_응답_목록.get(0).getName()).isEqualTo(메뉴그룹.getName());
    }

}
