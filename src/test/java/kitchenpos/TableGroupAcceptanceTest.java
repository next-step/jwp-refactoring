package kitchenpos;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static kitchenpos.MenuAcceptanceTest.메뉴_등록됨;
import static kitchenpos.MenuGroupAcceptanceTest.메뉴_그룹_등록됨;
import static kitchenpos.OrderAcceptanceTest.주문_등록됨;
import static kitchenpos.ProductAcceptanceTest.상품_등록됨;
import static kitchenpos.TableAcceptanceTest.테이블_등록됨;
import static kitchenpos.TableAcceptanceUtil.존재하지_않는_테이블_가져오기;
import static kitchenpos.TableAcceptanceUtil.주문이_들어간_테이블_가져오기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("단체 지정 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    private Menu 강정치킨;
    private OrderTable 주문이_들어간_테이블;
    private OrderTable 테이블1;
    private OrderTable 테이블2;
    private OrderTable 테이블3;
    private OrderTable 테이블4;

    private TableGroup 단체_지정1;
    private TableGroup 단체_지정2;

    @DisplayName("단체 지정 관련 기능 테스트")
    @TestFactory
    Stream<DynamicNode> product() {
        return Stream.of(
                dynamicTest("기초 데이터를 추가한다.", () -> {
                    Product 강정치킨_상품 = 상품_등록됨("강정치킨", BigDecimal.valueOf(15_000L));
                    MenuGroup 신메뉴 = 메뉴_그룹_등록됨("신메뉴");
                    강정치킨 = 메뉴_등록됨("강정치킨", BigDecimal.valueOf(15_000L), 신메뉴.getId(), 강정치킨_상품);

                    주문이_들어간_테이블 = 주문이_들어간_테이블_가져오기();
                    테이블1 = 테이블_등록됨(true, 0);
                    테이블2 = 테이블_등록됨(true, 0);
                    테이블3 = 테이블_등록됨(true, 0);
                    테이블4 = 테이블_등록됨(true, 0);
                }),
                dynamicTest("단체 지정을 등록한다.", () -> {
                    ResponseEntity<TableGroup> response = 단체_지정_생성_요청(테이블1, 테이블2);

                    단쳬_지정_생성됨(response);
                    단체_지정1 = response.getBody();
                }),
                dynamicTest("주문이 들어간 테이블이 포함된 단체 지정을 등록한다.", () -> {
                    ResponseEntity<TableGroup> response = 단체_지정_생성_요청(주문이_들어간_테이블, 테이블3);

                    단쳬_지정_생성_실패됨(response);
                }),
                dynamicTest("1개의 테이블로 단체 지정을 등록한다.", () -> {
                    ResponseEntity<TableGroup> response = 단체_지정_생성_요청(테이블4);

                    단쳬_지정_생성_실패됨(response);
                }),
                dynamicTest("존재하지 않는 테이블이 포함된 단체 지정을 등록한다.", () -> {
                    ResponseEntity<TableGroup> response = 단체_지정_생성_요청(테이블4, 존재하지_않는_테이블_가져오기());

                    단쳬_지정_생성_실패됨(response);
                }),
                dynamicTest("다른 단체 지정에 포함된 테이블이 포함된 단체 지정을 등록한다.", () -> {
                    ResponseEntity<TableGroup> response = 단체_지정_생성_요청(테이블4, 테이블1);

                    단쳬_지정_생성_실패됨(response);
                }),
                dynamicTest("단체 지정을 등록한다.", () -> {
                    ResponseEntity<TableGroup> response = 단체_지정_생성_요청(테이블3, 테이블4);

                    단쳬_지정_생성됨(response);
                    단체_지정2 = response.getBody();
                }),
                dynamicTest("단체 지정을 해지한다.", () -> {
                    ResponseEntity<Void> response = 단체_지정_해지_요청(단체_지정1);

                    단쳬_지정_해지됨(response);
                }),
                dynamicTest("주문이 들어간 테이블이 포함된 단체 지정은 단체 지정을 해지할 수 없다.", () -> {
                    주문_등록됨(테이블3, 강정치킨);

                    ResponseEntity<Void> response = 단체_지정_해지_요청(단체_지정2);

                    단쳬_지정_해지_실패됨(response);
                })
        );
    }

    public static ResponseEntity<TableGroup> 단체_지정_생성_요청(OrderTable... orderTables) {
        Map<String, Object> request = new HashMap<>();
        request.put("orderTables", Arrays.asList(orderTables));
        return restTemplate().postForEntity("/api/table-groups", request, TableGroup.class);
    }

    public static ResponseEntity<Void> 단체_지정_해지_요청(TableGroup tableGroup) {
        Map<String, Long> urlVariables = new HashMap<>();
        urlVariables.put("tableGroupId", tableGroup.getId());
        return restTemplate().exchange("/api/table-groups/{tableGroupId}", HttpMethod.DELETE,
                                       null, Void.class, urlVariables);
    }

    public static void 단쳬_지정_생성됨(ResponseEntity<TableGroup> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    public static void 단쳬_지정_생성_실패됨(ResponseEntity<TableGroup> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static void 단쳬_지정_해지됨(ResponseEntity<Void> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    public static void 단쳬_지정_해지_실패됨(ResponseEntity<Void> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
