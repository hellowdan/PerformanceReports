package gen;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import model.TestRecord;
import util.PropertiesUtil;
import util.SpreadSheetMap;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class SheetReport {

    private Sheets sheetsService;
    private Drive driveService;
    private String spreadSheetTemplateId;
    private String spreadSheetNewId;
    private String spreadSheetNewTitle;
    private String oldVersion;
    private String newVersion;

    public SheetReport() throws IOException, GeneralSecurityException {
        PropertiesUtil propertiesUtil = new PropertiesUtil();
        Properties properties = propertiesUtil.getProperties("perfrepo.properties");
        this.spreadSheetTemplateId = properties.getProperty("template_sheet_id");
        this.spreadSheetNewTitle = properties.getProperty("files_title");
        this.oldVersion = properties.getProperty("old_version");
        this.newVersion = properties.getProperty("new_version");
    }

    private void generateNewSheet() {
        File copiedFile = new File();
        copiedFile.setName(this.spreadSheetNewTitle);

        try {
            this.spreadSheetNewId = this.getDriveService().files().copy(this.spreadSheetTemplateId, copiedFile).execute().getId();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e);
        } finally {
            System.out.println("Fim");
        }
    }

    public void updateGoogleSheets() {

        try {
            generateNewSheet();
            updateValues();
            updateData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateValues() throws IOException {

        TestResults testResults = new TestResults();
        ValueRange body = new ValueRange();

        List values = new ArrayList();

        for (Integer key : SpreadSheetMap.spreadSheetPositions.keySet()) {
            for (TestRecord testRecord : testResults.getTestRecordList()) {
                if (testRecord.getHashCode() == SpreadSheetMap.spreadSheetPositions.get(key)) {
                    String score = testRecord.getScore();
                    values.add(Arrays.asList(score));
                    break;
                }
            }
        }

        body.setValues(values);
        UpdateValuesResponse result = this.getSheetsService().spreadsheets().values()
                .update(this.spreadSheetNewId, "J2", body)
                .setValueInputOption("USER_ENTERED")
                .execute();
    }


    public BatchUpdateSpreadsheetResponse updateData() throws IOException {
        List<Request> requests = new ArrayList<>();

        requests.add(new Request()
                .setUpdateSpreadsheetProperties(new UpdateSpreadsheetPropertiesRequest()
                        .setProperties(new SpreadsheetProperties()
                                .setTitle(this.spreadSheetNewTitle))
                        .setFields("title")));

        requests.add(new Request()
                .setFindReplace(new FindReplaceRequest()
                        .setFind("{{old_version}}")
                        .setReplacement(this.oldVersion)
                        .setAllSheets(true)));

        requests.add(new Request()
                .setFindReplace(new FindReplaceRequest()
                        .setFind("{{new_version}}")
                        .setReplacement(this.newVersion)
                        .setAllSheets(true)));

        BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);
        BatchUpdateSpreadsheetResponse response = this.getSheetsService().spreadsheets().batchUpdate(this.spreadSheetNewId, body).execute();
        FindReplaceResponse findReplaceResponse = response.getReplies().get(1).getFindReplace();
        System.out.printf("%d replacements made.", findReplaceResponse.getOccurrencesChanged());
        return response;
    }

    public Sheets getSheetsService() {
        return sheetsService;
    }

    public void setSheetsService(Sheets sheetsService) {
        this.sheetsService = sheetsService;
    }

    public Drive getDriveService() {
        return driveService;
    }

    public void setDriveService(Drive driveService) {
        this.driveService = driveService;
    }

    public String getSpreadSheetNewId() {
        return spreadSheetNewId;
    }
}
