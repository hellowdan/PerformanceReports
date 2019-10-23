package gen;

import com.google.api.services.docs.v1.Docs;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.sheets.v4.Sheets;
import util.PropertiesUtil;
import util.ServiceUtil;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Properties;

public class Report {

    private Docs docsService;
    private Drive driveService;
    private Sheets sheetService;
    private String folderNewId;
    private String folderNewTitle;
    private String resultParentFolderID;
    private String newDocID;
    private String newSpreadSheetID;

    public Report() throws IOException, GeneralSecurityException {

        PropertiesUtil propertiesUtil = new PropertiesUtil();
        Properties properties = propertiesUtil.getProperties("perfrepo.properties");
        this.folderNewTitle = properties.getProperty("folder_title");
        this.resultParentFolderID = properties.getProperty("result_parent_folder_id");

        ServiceUtil serviceUtil = new ServiceUtil();
        this.setDocsService(serviceUtil.getDocs());
        this.setSheetService(serviceUtil.getSheets());
        this.setDriveService(serviceUtil.getDrive());
    }

    public void updateReports() throws IOException, GeneralSecurityException {
        createNewDir();
        createNewSpreadSheet();
        createNewDoc();
        moveFiles();
    }

    private void createNewDir() throws IOException {
        File body = new File();
        body.setName(this.folderNewTitle);
        body.setParents(Arrays.asList(this.resultParentFolderID));
        body.setMimeType("application/vnd.google-apps.folder");
        File file = driveService.files().create(body).setFields("id").execute();
        this.folderNewId = file.getId();
    }

    private void createNewDoc() throws IOException, GeneralSecurityException {
        DocReport docReport = new DocReport();
        docReport.setLinkedSheetID(this.newSpreadSheetID);
        docReport.setDocsService(getDocsService());
        docReport.setDriveService(getDriveService());
        docReport.updateGoogleDocs();
        this.newDocID = docReport.getDocNewId();
    }

    private void createNewSpreadSheet() throws IOException, GeneralSecurityException {
        SheetReport sheetReport = new SheetReport();
        sheetReport.setSheetsService(getSheetService());
        sheetReport.setDriveService(getDriveService());
        sheetReport.updateGoogleSheets();
        this.newSpreadSheetID = sheetReport.getSpreadSheetNewId();
    }

    private void moveFiles() {
        try {
            File sheetToMove = driveService.files().get(this.newSpreadSheetID)
                    .setFields("parents")
                    .execute();
            File docToMove = driveService.files().get(this.newDocID)
                    .setFields("parents")
                    .execute();

            StringBuilder previousParents = new StringBuilder();
            for (String parent : sheetToMove.getParents()) {
                previousParents.append(parent);
                previousParents.append(',');
            }
            for (String parent : docToMove.getParents()) {
                previousParents.append(parent);
                previousParents.append(',');
            }

            driveService.files().update(this.newSpreadSheetID, null)
                    .setAddParents(this.folderNewId)
                    .setRemoveParents(previousParents.toString())
                    .setFields("id, parents")
                    .execute();

            driveService.files().update(this.newDocID, null)
                    .setAddParents(this.folderNewId)
                    .setRemoveParents(previousParents.toString())
                    .setFields("id, parents")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Docs getDocsService() {
        return docsService;
    }

    public void setDocsService(Docs docsService) {
        this.docsService = docsService;
    }

    public Drive getDriveService() {
        return driveService;
    }

    public void setDriveService(Drive driveService) {
        this.driveService = driveService;
    }

    public Sheets getSheetService() {
        return sheetService;
    }

    public void setSheetService(Sheets sheetService) {
        this.sheetService = sheetService;
    }

}
