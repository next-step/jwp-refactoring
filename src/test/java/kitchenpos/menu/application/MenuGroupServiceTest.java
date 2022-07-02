package kitchenpos.menu.application;


import static kitchenpos.fixture.MenuFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;
    @InjectMocks
    private MenuGroupService menuGroupService;
    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = 메뉴그룹_생성(1L, "파스타");
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        //given
        given(menuGroupRepository.save(any())).willReturn(menuGroup);
        MenuGroupRequest menuGroupRequest = 메뉴그룹_생성_요청(menuGroup.getName());

        //when
        MenuGroupResponse createdMenuGroupResponse = menuGroupService.create(menuGroupRequest);

        //then
        assertThat(createdMenuGroupResponse).isNotNull();
        assertThat(createdMenuGroupResponse.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        //given
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(menuGroup, 메뉴그룹_생성(2L, "피자")));

        //when
        List<MenuGroupResponse> menuGroupResponses = menuGroupService.list();

        //then
        assertThat(menuGroupResponses).hasSize(2);
    }


}
