package com.codewithsayan.warrantywallet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class ReceiptStorageService {
    private static final Set<String> ALLOWED=Set.of(".pdf",".png",".jpg",".jpeg");
    private final Path directory;

    public ReceiptStorageService(@Value("${warrantywallet.upload-dir:./uploads/receipts}") String dir) {
        directory=Paths.get(dir).toAbsolutePath().normalize();
        try { Files.createDirectories(directory); }
        catch(IOException ex){ throw new IllegalStateException("Could not initialise receipt storage.",ex); }
    }

    public String store(MultipartFile file) {
        if(file==null || file.isEmpty()) return null;
        String name=file.getOriginalFilename()==null ? "" : file.getOriginalFilename();
        String lower=name.toLowerCase(Locale.ROOT);
        int dot=lower.lastIndexOf('.');
        String extension=dot>=0 ? lower.substring(dot) : "";
        if(!ALLOWED.contains(extension)) throw new IllegalArgumentException("Receipt must be PDF, PNG or JPG.");
        if(file.getSize()>8L*1024*1024) throw new IllegalArgumentException("Receipt must be 8 MB or smaller.");
        String generated=UUID.randomUUID()+extension;
        Path target=directory.resolve(generated).normalize();
        if(!target.startsWith(directory)) throw new IllegalArgumentException("Invalid receipt filename.");
        try { Files.copy(file.getInputStream(),target,StandardCopyOption.REPLACE_EXISTING); return generated; }
        catch(IOException ex){ throw new IllegalStateException("Could not save receipt.",ex); }
    }

    public Resource load(String filename) {
        if(filename==null || filename.contains("/") || filename.contains("\\") || filename.contains(".."))
            throw new IllegalArgumentException("Invalid receipt reference.");
        try {
            Path file=directory.resolve(filename).normalize();
            if(!file.startsWith(directory) || !Files.exists(file)) throw new IllegalArgumentException("Receipt was not found.");
            Resource resource=new UrlResource(file.toUri());
            if(!resource.exists() || !resource.isReadable()) throw new IllegalArgumentException("Receipt is not readable.");
            return resource;
        } catch(IOException ex){ throw new IllegalArgumentException("Could not open receipt.",ex); }
    }

    public void delete(String filename) {
        if(filename==null || filename.contains("/") || filename.contains("\\") || filename.contains("..")) return;
        try { Files.deleteIfExists(directory.resolve(filename).normalize()); }
        catch(IOException ignored) {}
    }
}
