package kitchenpos.utils;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class ResponseUtil {
    public static long getLocationCreatedId(ExtractableResponse<Response> createdResponse) {
        return Long.parseLong(createdResponse.header("Location").split("/")[3]);
    }
}
