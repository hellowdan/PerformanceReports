package gen;

import model.TestRecord;
import util.ParserCSVUtil;
import util.PropertiesUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class TestResults {

    private String buildtimeCsvPath;
    private String runtimeCsvPath;
    private List<TestRecord> testRecordList;

    public TestResults() throws IOException {
        PropertiesUtil propertiesUtil = new PropertiesUtil();
        Properties properties = propertiesUtil.getProperties("perfrepo.properties");

        buildtimeCsvPath = properties.getProperty("buildtime_csv_path");
        runtimeCsvPath = properties.getProperty("runtime_csv_path");

    }

    List<TestRecord> getTestRecordList() throws IOException {
        this.testRecordList = new ArrayList<>();
        this.testRecordList.addAll(getDataFromBuildTimeCSV());
        this.testRecordList.addAll(getDataFromRunTimeCSV());

        return testRecordList;
    }

    private List<TestRecord> getDataFromBuildTimeCSV() throws IOException {
        List<TestRecord> testRecordList = new ArrayList<>();

        Scanner scanner = new Scanner(new File(buildtimeCsvPath));
        try {
            while (scanner.hasNext()) {
                List<String> line = ParserCSVUtil.parseLine(scanner.nextLine());
                TestRecord testRecord = new TestRecord();
                testRecord.setName(line.get(0));
                testRecord.setNumberOfRules(line.get(8));
                testRecord.setNrOfRules(line.get(7));
                testRecord.setUseCanonicalModel(line.get(10));
                testRecord.setRulesProviderId(line.get(9));
                testRecord.setScore(line.get(4));
                testRecord.setHashCode();

                testRecordList.add(testRecord);
            }
        } finally {
            scanner.close();
        }

        return testRecordList;
    }

    private List<TestRecord> getDataFromRunTimeCSV() throws IOException {
        List<TestRecord> testRecordList = new ArrayList<>();

        Scanner scanner = new Scanner(new File(runtimeCsvPath));
        try {
            while (scanner.hasNext()) {
                List<String> line = ParserCSVUtil.parseLine(scanner.nextLine());
                TestRecord testRecord = new TestRecord();
                testRecord.setName(line.get(0));
                testRecord.setScore(line.get(4));
                testRecord.setMatchRatio(line.get(7));
                testRecord.setHashCode();

                testRecordList.add(testRecord);
            }

        } finally {
            scanner.close();
        }

        return testRecordList;
    }
}