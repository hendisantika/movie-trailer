package id.my.hendisantika.movietrailer.controller;

import id.my.hendisantika.movietrailer.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by IntelliJ IDEA.
 * Project : movie-trailer
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 29/10/25
 * Time: 06.17
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping("/assets")
@RequiredArgsConstructor
public class AssetsController {
    private final WarehouseService warehouseService;

    @GetMapping("/{filename:.+}")
    public Resource getResource(@PathVariable("filename") String filename) {
        return warehouseService.uploadAsResource(filename);
    }
}
