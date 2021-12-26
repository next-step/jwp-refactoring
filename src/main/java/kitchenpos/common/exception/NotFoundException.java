package kitchenpos.common.exception;

/**
 * packageName : kitchenpos.common.exception
 * fileName : NotFoundException
 * author : haedoang
 * date : 2021/12/26
 * description :
 */
public class NotFoundException extends ServiceException {
    private static final Long serialVersionUID = 1L;

    public NotFoundException(String message) {
        super(message);
    }
}
