package kitchenpos;

import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Stream;

import static kitchenpos.MenuGroupAcceptanceTestUtil.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("메뉴 그룹 관련 기능")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 그룹 관련 기능 테스트")
    @TestFactory
    Stream<DynamicNode> menuGroup() {
        return Stream.of(
                dynamicTest("메뉴 그룹을 등록한다.", () -> {
                    ResponseEntity<MenuGroupResponse> response = 메뉴_그룹_생성_요청("신메뉴");

                    메뉴_그룹_생성됨(response);
                }),
                dynamicTest("이름이 없는 메뉴 그룹을 등록한다.", () -> {
                    ResponseEntity<MenuGroupResponse> response = 메뉴_그룹_생성_요청(null);

                    메뉴_그룹_생성_실패됨(response);
                }),
                dynamicTest("메뉴 그룹 목록을 조회한다.", () -> {
                    ResponseEntity<List<MenuGroupResponse>> response = 메뉴_그룹_목록_조회_요청();

                    메뉴_그룹_목록_응답됨(response);
                    메뉴_그룹_목록_확인됨(response, "신메뉴");
                })
        );
    }
}
