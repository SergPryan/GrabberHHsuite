import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class HHgrabber {
    private static final String URL_HH="https://api.hh.ru/vacancies?area=1202&specialization=1";
    private static final String COUNT_VACANCIES_ON_PAGE ="per_page=100";
    private static List<Vacancy> vacancies;

    public static List<Vacancy> getVacancies() {
        return vacancies;
    }

    static {
        try {
            String responseSite = getVacancy();
            vacancies = parseVacancies(responseSite);
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
        }
    }


    private static String getVacancy() throws IOException {
        URL URL=new URL(URL_HH+"&"+ COUNT_VACANCIES_ON_PAGE);
        HttpURLConnection httpURLConnection=(HttpURLConnection) URL.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
        httpURLConnection.connect();
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
        String result=br.readLine();
        br.close();
        httpURLConnection.disconnect();
        return result;
    }
    private static List<Vacancy> parseVacancies(String responseSite) throws ParseException {
        List<Vacancy> result=new ArrayList<Vacancy>();
        JSONParser jsonParser=new JSONParser();
        JSONArray jsonArray=(JSONArray) ((JSONObject) jsonParser.parse(responseSite)).get("items");
        System.out.println(jsonArray);
        for (Object o : jsonArray) {
            Vacancy vacancy = new Vacancy();
            JSONObject tmp = (JSONObject) o;
            vacancy.setName((String) tmp.get("name"));
            vacancy.setDateOfPublication((String) tmp.get("published_at"));
            vacancy.setUrl((String) tmp.get("alternate_url"));
            if (tmp.get("salary") != null) {
                StringBuilder stringBuilder = new StringBuilder();
                JSONObject salary = (JSONObject) jsonParser.parse(((JSONObject) tmp.get("salary")).toJSONString());
                Object to = salary.get("to");
                Object from = salary.get("from");
                if (to != null) {
                    stringBuilder.append("to ").append(to.toString()).append(" ");
                }
                if (from != null) {
                    stringBuilder.append("from ").append(from.toString());
                }
                vacancy.setSalary(stringBuilder.toString());
            }
            JSONObject employer = (JSONObject) tmp.get("employer");
            vacancy.setOrganization(employer.get("name").toString());
            result.add(vacancy);
        }
        return result;
    }
}
