package ru.bezfy.ed_helper_api.service.google;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.client.http.FileContent;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleDriveUploadService {

    private final GoogleDriveService googleDriveService;

    public GoogleDriveUploadService(GoogleDriveService googleDriveService) {
        this.googleDriveService = googleDriveService;
    }

    public String uploadFile(java.io.File filePath, String mimeType, String folderId) throws GeneralSecurityException, IOException {
        Drive driveService = googleDriveService.getDriveService();

        File fileMetadata = new File();
        fileMetadata.setName(filePath.getName());
        fileMetadata.setParents(Collections.singletonList(folderId));// ID папки на Google Drive

        FileContent mediaContent = new FileContent(mimeType, filePath);

        File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();

        return uploadedFile.getId(); // Возвращаем ID загруженного файла
    }
}
