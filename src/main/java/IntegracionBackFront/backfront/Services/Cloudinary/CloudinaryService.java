package IntegracionBackFront.backfront.Services.Cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    //Constante define el tamaño maximo permitido par los archivos
    private  static  final long MAX_FILE_SIZE = 5 * 1024 *1024;
    //COSNTANTE PARA DEFINIR LOS TIPOS DE ARCHIVOS PERMITIDOS
    private static  final String[] ALLOWED_EXTENSIONS = {".jpg",".jpeg", ".png" };
    //Cliente de Cloudinary como dependencia
    private  final Cloudinary cloudinary;


    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    /**
     * Subir imagenes a la raiz de cloudinary
     * @param file
     * @return la URL de la imagen
     * @throws IOException
     */
    public  String uploadImage(MultipartFile file) throws IOException {
        validateImage(file);

        //Sube el archivo a cloudinary con configuraciones básicas
        //Tipo de recurso auto-detectado
        //Calidad automática
        Map<?,?> uploadResult = cloudinary.uploader()
                .upload(file.getBytes() , ObjectUtils.asMap(
                        "resource_type", "auto", "quality", "auto:good"
                ));
        return (String) uploadResult.get("secure_url");
    }

    /**
     *Subo una imagen a una carpeta en especifico
     * @param file
     * @param folder carpeta destino
     * @return URL segura (HTTPS) de la imagen subida
     * @throws IOException
     */
    public  String uploadImage(MultipartFile file, String folder) throws  IOException{
        validateImage(file);
        //Generar un nombre unico para el archivo
        //Conservar la extensión general
        //Agregar un refijo y un UUID epara evitar colisiones
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFileName = "img_" + UUID.randomUUID() + fileExtension;

        //Confiuración para subir imagen
        Map<String, Object> options = ObjectUtils.asMap(
          "folder", folder,                 //Carpeta destino
                  "public_id", uniqueFileName,      //Nombre unico para el archivo
                  "use_filename", false,            //No usar el nombre original
                  "unique_filename", false,         //NO generar nombre uncio (proceso hecho anteriormente)
                  "overwrite", false,               //No sobreescribir archivo
                  "resource_type", "auto",          //Auto-detectar tipo de recurso
                  "quality", "auto:good"            //Optimizar la calidad automática
        );

        Map<?,?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
        return (String) uploadResult.get("secure_url");
    }

    /**
     *
     * @param file
     */
    private void validateImage(MultipartFile file){
        //1.Verificar si el archivo esta vacio
        if (file.isEmpty()){
            throw  new IllegalArgumentException("el archivo no puede estar vacio");
        }
        //2.Verificar el tamaño de la imagen
        if (file.getSize() > MAX_FILE_SIZE){
            throw  new IllegalArgumentException("El archivo no puede ser mayor a 5MB");
        }
        //3. Obtener y validar el nombre original del archivo
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null){
            throw  new IllegalArgumentException("Nombre de archivo inválido");
        }
        //4.Extraer y validar la extensión
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")).toLowerCase();
        if (!Arrays.asList(ALLOWED_EXTENSIONS).contains(extension)){
            throw  new IllegalArgumentException("Solo se permite archivos JPG, JPEG, PNG");
        }

        //5.Verifica el tipo de MIME sea una imagen
        if (!file.getContentType().startsWith("image/")){
            throw  new IllegalArgumentException("El archivo dbe ser una imagen válida");
        }



    }


}
