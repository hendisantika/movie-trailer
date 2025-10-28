package id.my.hendisantika.movietrailer.exception;

/**
 * Created by IntelliJ IDEA.
 * Project : movie-trailer
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 29/10/25
 * Time: 06.14
 * To change this template use File | Settings | File Templates.
 */
public class WarehouseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public WarehouseException(String message) {
        super(message);
    }

    public WarehouseException(String message, Throwable exception) {
        super(message, exception);
    }
}
