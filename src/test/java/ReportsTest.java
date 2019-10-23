import gen.Report;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class ReportsTest {

    @Test
    public void loadDataFromFileTest() throws IOException, GeneralSecurityException {

        Report report = new Report();
        report.updateReports();
    }

}
