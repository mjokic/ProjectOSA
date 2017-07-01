package sf282015.osa.projectOSA.service.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

//    private final Path rootLocation = Paths.get("/");
	private Path rootLocation;

//    @Autowired
//    public FileSystemStorageService(StorageProperties properties) {
//        this.rootLocation = Paths.get(properties.getLocation());
//    }

//    @Value(value = "classpath:static/images")
//    private Resource resource;
    
    public String storeItemPicture(MultipartFile file) {
    	String imagesFolder = null;
		imagesFolder = "static/images/items";
    	
		String[] tmp = file.getOriginalFilename().split("\\.");
		
		String extension = tmp[1];
		
		if(!extension.equals("png") && !extension.equals("svg") &&
				!extension.equals("jpg") && !extension.equals("jpeg"))
			return null;
		
    	InputStream inputStream = null;
        OutputStream outputStream = null;
        String fileName = 
        		String.valueOf(new Timestamp(System.currentTimeMillis()).getTime())+"."+extension;
        File newFile = new File(imagesFolder + "/"+fileName);

        System.out.println(newFile.getAbsolutePath() + "ABS PATH");
        
        try {
            inputStream = file.getInputStream();

            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        return fileName;
    }


    public String storeUserAvatar(MultipartFile file) {
    	String imagesFolder = null;
		imagesFolder = "static/images/avatar";
    	
		String[] tmp = file.getOriginalFilename().split("\\.");
		
		String extension = tmp[1];
		
		if(!extension.equals("png") && !extension.equals("svg") &&
				!extension.equals("jpg") && !extension.equals("jpeg"))
			return null;
		
    	InputStream inputStream = null;
        OutputStream outputStream = null;
        String fileName = 
        		String.valueOf(new Timestamp(System.currentTimeMillis()).getTime())+"."+extension;
        File newFile = new File(imagesFolder + "/"+fileName);

        try {
            inputStream = file.getInputStream();

            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        return fileName;
    }

    
    
    
    
    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
