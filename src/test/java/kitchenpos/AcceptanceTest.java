package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.Function;
import java.util.function.ToLongFunction;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.utils.DatabaseCleanUtils;
import kitchenpos.utils.RestAssuredUtils;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest<T> {

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanUtils databaseCleanup;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.config = RestAssured.config()
            .objectMapperConfig(
                new ObjectMapperConfig().jackson2ObjectMapperFactory((cls, charset) -> objectMapper));
        databaseCleanup.cleanUp();
    }

    public ExtractableResponse<Response> 등록_요청(T requestBody) {
        return 등록_요청(getRequestPath(), requestBody);
    }

    private ExtractableResponse<Response> 등록_요청(String requestPath, T requestBody) {
        return RestAssuredUtils.post(requestPath, requestBody);
    }

    public T 등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.body().as(getDomainClass());
    }

    protected void 등록_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isNotEqualTo(HttpStatus.OK.value());
    }

    protected List<T> 목록_조회() {
        ExtractableResponse<Response> response = RestAssuredUtils.get(getRequestPath());
        return response.body().jsonPath().getList(".", getDomainClass());
    }

    protected void 목록_조회됨(List<T> actualList, Long expectedId) {
        assertThat(actualList)
            .extracting(idExtractor()::applyAsLong)
            .contains(expectedId);
    }

    protected ExtractableResponse<Response> 수정_요청(String requestPath, T requestBody) {
        return RestAssuredUtils.put(requestPath,
                                    idExtractor().applyAsLong(requestBody),
                                    requestBody);
    }

    protected void 수정됨(ExtractableResponse<Response> response, T expected,
        Function<T, ?> compareExtractor) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<T> actualList = 목록_조회();
        T actual = actualList.stream()
            .filter(body -> isEqual(expected, body))
            .findFirst().get();

        assertThat(isEqual(actual, expected, compareExtractor)).isTrue();
    }

    protected void 수정_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isNotEqualTo(HttpStatus.OK.value());
    }

    protected ExtractableResponse<Response> 삭제_요청(String requestPath, Long id) {
        return RestAssuredUtils.delete(requestPath, id);
    }

    protected void 삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    protected void 삭제_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isNotEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private boolean isEqual(T actual, T expected, Function<T,?> compareExtractor) {
        return compareExtractor.apply(actual).equals(compareExtractor.apply(expected));
    }

    private boolean isEqual(T expected, T body) {
        return idExtractor().applyAsLong(expected) == idExtractor().applyAsLong(body);
    }

    protected abstract String getRequestPath();

    protected abstract ToLongFunction<T> idExtractor();

    protected abstract Class<T> getDomainClass();
}
