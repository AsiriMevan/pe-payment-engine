package lk.dialog.pe.billing.testutil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestPayloadUtil {

    public static String getJsonString(String... path) {
        List<String> fullPath = new ArrayList<>(Arrays.asList("test", "resources","requestpayloads"));
        List<String> otherPath = Arrays.asList(path);
        fullPath.addAll(otherPath);
        String[] pathArray = fullPath.toArray(new String[0]);
        String json = null;
        try {
            Path resourcePath = Paths.get("src", pathArray);
            json = new String(Files.readAllBytes(resourcePath));
        } catch (Exception exception) {
            System.out.println(Arrays.toString(exception.getStackTrace()));
        }
        if(json != null) {
            json = json.trim();
        }
        return json;
    }

    public static String getJsonString(PayloadPath path) {
        return getJsonString(path.getPath());
    }
}
