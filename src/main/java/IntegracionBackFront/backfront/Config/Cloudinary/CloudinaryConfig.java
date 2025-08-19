package IntegracionBackFront.backfront.Config.Cloudinary;

import com.cloudinary.Cloudinary;
import com.google.api.client.util.Value;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    //Variables para almacenar las credenciales de cloudinary
    private String cloudName;
    private  String  apiKey;
    private  String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        //crear objeto de tipo Dotenv
        Dotenv dotenv = Dotenv.load();

        //Crear un MAp guardar clave valor del archivo .env
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", dotenv.get("CLOUDINARY_CLOUD_NAME")); //Nombre de la nube en cloudinary
        config.put("api_key", dotenv.get("CLOUDINARY_API_KEY")); //Api ey autenticación
        config.put("api_secret", dotenv.get("CLOUDINARY_API_SECRET")); //Clave secreta

        //Retornamos una nueva instancia de LCoudinary con la configuración cargada
        return new Cloudinary(config);
    }


}
