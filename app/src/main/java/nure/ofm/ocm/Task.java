package nure.ofm.ocm;

public class Task {

    String title;
    String executor;
    String dueTo;
    String description;


    Task(String _title, String _executor, String _dueTo, String _description) {
        title = _title;
        executor = _executor;
        dueTo = _dueTo;
        description = _description;
    }
}
