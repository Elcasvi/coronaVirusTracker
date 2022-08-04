package virtualtek.com.coronaVirustracker.controler;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import virtualtek.com.coronaVirustracker.models.LocationStats;
import virtualtek.com.coronaVirustracker.services.CoronaVirusDataService;

import java.text.DecimalFormat;
import java.util.List;


@Controller
public class HomeController {
    @Autowired
    CoronaVirusDataService coronaVirusDataService;
    @GetMapping("/")
    public String home(Model model)
    {
        List<LocationStats> allStats=coronaVirusDataService.getAllStats();
        int totalCases=allStats.stream().mapToInt(stat->stat.getLatestTotalCases()).sum();
        int totalNewCases=allStats.stream().mapToInt(stat->stat.getDiffFromPrevDay()).sum();

        model.addAttribute("locationStats",coronaVirusDataService.getAllStats());
        model.addAttribute("totalReportedCases",totalCases);
        model.addAttribute("totalNewReportedCases",totalNewCases);

        return "home";
    }

}
