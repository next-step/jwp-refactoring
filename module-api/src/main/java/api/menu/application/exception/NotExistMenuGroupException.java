package api.menu.application.exception;

import common.error.NotExistException;

public class NotExistMenuGroupException extends NotExistException {
    public NotExistMenuGroupException() {
        super("해당하는 메뉴 그룹을 찾을 수 없습니다.");
    }

    public NotExistMenuGroupException(String message) {
        super(message);
    }
}
