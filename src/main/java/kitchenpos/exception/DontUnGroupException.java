package kitchenpos.exception;

public class DontUnGroupException extends RuntimeException {
    private static final String DONT_UNGROUP_MESSAGE = "단체 해제를 할 수 없는 상태입니다.";

    public DontUnGroupException() {
        super(DONT_UNGROUP_MESSAGE);
    }
}
