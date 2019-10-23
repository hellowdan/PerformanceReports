package gen;

import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.model.*;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import util.PropertiesUtil;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DocReport {
    private Docs docsService;
    private Drive driveService;
    private String docTemplateId;
    private String linkedSheetID;
    private String docNewId;
    private String docNewTitle;
    private String oldVersion;
    private String newVersion;
    private String reportDate;
    private String author;
    private String emailAuthor;
    private String results;

    public DocReport() throws IOException {
        PropertiesUtil propertiesUtil = new PropertiesUtil();
        Properties properties = propertiesUtil.getProperties("perfrepo.properties");
        this.docTemplateId = properties.getProperty("template_doc_id");
        this.docNewTitle = properties.getProperty("files_title");
        this.oldVersion = properties.getProperty("old_version");
        this.newVersion = properties.getProperty("new_version");
        this.reportDate = properties.getProperty("report_date");
        this.author = properties.getProperty("author");
        this.emailAuthor = properties.getProperty("email_author");
        this.results = properties.getProperty("results");
    }

    private void generateNewDoc() {
        File copiedFile = new File();
        copiedFile.setName(this.docNewTitle);

        try {
            this.docNewId = this.getDriveService().files().copy(this.docTemplateId, copiedFile).execute().getId();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        } finally {
            System.out.println("Fim");
        }
    }

    public void updateGoogleDocs() {

        try {
            generateNewDoc();
            updateData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void UpdateLinkedSheet(){

    }

    private BatchUpdateDocumentResponse updateData() throws IOException {
        List<Request> requests = new ArrayList<>();
        requests.add(new Request()
                .setReplaceAllText(new ReplaceAllTextRequest()
                        .setContainsText(new SubstringMatchCriteria()
                                .setText("{{new_version}}")
                                .setMatchCase(true))
                        .setReplaceText(this.newVersion)));
        requests.add(new Request()
                .setReplaceAllText(new ReplaceAllTextRequest()
                        .setContainsText(new SubstringMatchCriteria()
                                .setText("{{old_version}}")
                                .setMatchCase(true))
                        .setReplaceText(this.oldVersion)));
        requests.add(new Request()
                .setReplaceAllText(new ReplaceAllTextRequest()
                        .setContainsText(new SubstringMatchCriteria()
                                .setText("{{author}}")
                                .setMatchCase(true))
                        .setReplaceText(this.author)));
        requests.add(new Request()
                .setReplaceAllText(new ReplaceAllTextRequest()
                        .setContainsText(new SubstringMatchCriteria()
                                .setText("{{email_author}}")
                                .setMatchCase(true))
                        .setReplaceText(this.emailAuthor)));
        requests.add(new Request()
                .setReplaceAllText(new ReplaceAllTextRequest()
                        .setContainsText(new SubstringMatchCriteria()
                                .setText("{{report_date}}")
                                .setMatchCase(true))
                        .setReplaceText(this.reportDate)));
        requests.add(new Request()
                .setReplaceAllText(new ReplaceAllTextRequest()
                        .setContainsText(new SubstringMatchCriteria()
                                .setText("{{results}}")
                                .setMatchCase(true))
                        .setReplaceText(this.results)));

        BatchUpdateDocumentRequest body = new BatchUpdateDocumentRequest();
        BatchUpdateDocumentResponse response = getDocsService().documents().batchUpdate(this.docNewId, body.setRequests(requests)).execute();
        return response;
    }

    private Docs getDocsService() {
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

    public String getDocNewId() {
        return docNewId;
    }

    public String getLinkedSheetID() {
        return linkedSheetID;
    }

    public void setLinkedSheetID(String linkedSheetID) {
        this.linkedSheetID = linkedSheetID;
    }
}
