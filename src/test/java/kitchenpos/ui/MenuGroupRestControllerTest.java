package kitchenpos.ui;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MenuGroupRestControllerTest {

    private MenuGroup 세마리메뉴;
    private MenuGroup 패밀리메뉴;

    @Autowired
    private MenuGroupRestController controller;

    @BeforeEach
    void setUp() {
        세마리메뉴 = new MenuGroup();
        세마리메뉴.setName("세마리메뉴");

        패밀리메뉴 = new MenuGroup();
        패밀리메뉴.setName("패밀리메뉴");
    }

    @DisplayName("상품명과 가격 정보를 입력해 상품을 등록한다")
    @Test
    void createMenuGroup() {
        // given when
        ResponseEntity<MenuGroup> menuGroupResponse = controller.create(세마리메뉴);

        // then
        MenuGroup actual = menuGroupResponse.getBody();
        assertThat(actual).isNotNull();
        assertThat(menuGroupResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(menuGroupResponse.getHeaders().getLocation().toString()).isEqualTo("/api/menu-groups/" + actual.getId());
        assertThat(actual.getName()).isEqualTo(세마리메뉴.getName());
    }

    @DisplayName("메뉴그룹 목록을 조회한다")
    @Test
    void findAll() {
        // given
        controller.create(세마리메뉴);
        controller.create(패밀리메뉴);

        // then
        ResponseEntity<List<MenuGroup>> responseEntity = controller.list();
        List<MenuGroup> products = responseEntity.getBody();
        List<String> actual = products.stream()
                .map(MenuGroup::getName)
                .collect(Collectors.toList());

        // then
        assertThat(actual).containsAll(Arrays.asList("두마리메뉴", "한마리메뉴", "순살파닭두마리메뉴", "신메뉴", "세마리메뉴", "패밀리메뉴"));
    }
}