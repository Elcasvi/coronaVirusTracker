package virtualtek.com.coronaVirustracker.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import virtualtek.com.coronaVirustracker.models.LocationStats;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDataService {
    private List<LocationStats>allStats =new ArrayList<>();

    public List<LocationStats> getAllStats()
    {
        return allStats;
    }
    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStats>newStats=new ArrayList<>();
        String VIRUS_DATA_URL="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
        //System.out.println("Hello world from the model side");
        HttpClient client=HttpClient.newHttpClient();
        HttpRequest request=HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();

        HttpResponse<String> httpResponse= client.send(request, HttpResponse.BodyHandlers.ofString());
        //System.out.println(httpResponse.body());

        StringReader csvBodyReader=new StringReader(httpResponse.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        for (CSVRecord record : records) {
            LocationStats locationStats=new LocationStats();
            String state = record.get("Province/State");
            String country = record.get("Country/Region");
            String totalCases=record.get(record.size()-1);
            String totalCasesPrevDay=record.get(record.size()-2);

            locationStats.setState(state);
            locationStats.setCountry(country);
            locationStats.setLatestTotalCases(Integer.parseInt(totalCases));
            locationStats.setDiffFromPrevDay(Integer.parseInt(totalCases)-Integer.parseInt(totalCasesPrevDay));


            System.out.println(locationStats);
            newStats.add(locationStats);
        }
        allStats=newStats;
    }
}