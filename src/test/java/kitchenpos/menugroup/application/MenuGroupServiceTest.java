package kitchenpos.menugroup.application;

import static kitchenpos.util.TestDataSet.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("그룹을 생성 정상 성공 케이스")
    void create() {
        //given
        given(menuGroupRepository.save(any())).willReturn(추천_메뉴_그륩);
        MenuGroupRequest request = new MenuGroupRequest("추천 메뉴");
        //when
        MenuGroupResponse result = menuGroupService.create(request);

        // then
        assertThat(result.getName()).isEqualTo(추천_메뉴_그륩.getName());
        verify(menuGroupRepository, times(1)).save(any());
    }

}
