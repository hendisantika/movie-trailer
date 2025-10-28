package id.my.hendisantika.movietrailer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by IntelliJ IDEA.
 * Project : movie-trailer
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 29/10/25
 * Time: 06.13
 * To change this template use File | Settings | File Templates.
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class FileNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String message, Throwable exception) {
        super(message, exception);
    }
}
