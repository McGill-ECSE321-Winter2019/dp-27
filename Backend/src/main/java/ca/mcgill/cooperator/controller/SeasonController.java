package ca.mcgill.cooperator.controller;

import ca.mcgill.cooperator.model.Season;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("seasons")
@RestController
@CrossOrigin(origins = "*")
public class SeasonController {

    @GetMapping("")
    public List<String> getSeasons() {
        List<String> l = new ArrayList<>();
        Season[] seasons = Season.class.getEnumConstants();
        for (Season s : seasons) {
            l.add(s.name());
        }
        return l;
    }
}
