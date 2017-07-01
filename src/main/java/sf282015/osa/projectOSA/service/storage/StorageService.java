package sf282015.osa.projectOSA.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

//    void store(MultipartFile file);
    
    String storeItemPicture(MultipartFile file);
    
    String storeUserAvatar(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

}
