package IntegracionBackFront.backfront.Controller.Cloudinary;

import IntegracionBackFront.backfront.Services.Cloudinary.CloudinaryService;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/image")
@CrossOrigin
public class ImageController {
    @Autowired
    private  final CloudinaryService cloudinaryService;

    public ImageController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?>  uploadImage(@RequestParam("image")MultipartFile file){
        try{
            String imageUrl= cloudinaryService.uploadImage(file);
            return  ResponseEntity.ok(Map.of(
                "message", "Iagen subida exitosamente",
                "url", imageUrl
            ));
        }catch (IOException e){
            return  ResponseEntity.internalServerError().body("Error al subir la imagen");
        }
    }

    @PostMapping("/upload-to-folder")
    public  ResponseEntity<?> uploadImageFlorder(
            @RequestParam("image") MultipartFile file,
            @RequestParam String folder
    ){
        try{
            String imageUrl = cloudinaryService.uploadImage(file,folder);
            System.out.println("URI 1: " + imageUrl);
            return ResponseEntity.ok(Map.of(
                    "Message", "Imagen Subida exitosamente",
                    "url", imageUrl
            ));
        } catch (IOException e) {
            return  ResponseEntity.internalServerError().body("Error al subir la imagen");
        }

    }
}
