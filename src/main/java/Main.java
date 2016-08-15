import java.util.List;


public class Main {
    public static void main(String[] args) {
        List<Vacancy> list=HHgrabber.getVacancies();
        DatabaseManager.createRecord(list);
    }
}
