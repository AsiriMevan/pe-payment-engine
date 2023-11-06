package lk.dialog.pe.scheduler.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
@Slf4j
public class SchUtil {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static String convertDate(Date date){
        try {
            return dateFormat.format(date);
        }
        catch (Exception e){
            log.error("convertDate failed date={}",date,e);
            return null;
        }
    }

    public static String convertPaymentMode(String payMode){
        if(payMode==null)return null;
        String result;
        switch (payMode.toUpperCase()){
            case "DDB": case"DDBIT": result="Direct Debit";break;
            case "SP": case"STAR": result="Star Points";break;
            case "MIS": case"MISCE": result="Misc Payments(vou)";break;
            case "MS2": case"MISC2": result="Misc Payments(ori branch)";break;
            case "CA": case"CASH": result="Cash Only";break;
            case "CC": case"CARD": result="Credit Card";break;
            case "CHE": case"CHEQ": result="Cheque Only";break;
            case "DRNOTE": result="Debit Notes";break;
            case "CRNOTE": result="Credit Notes";break;
            default: result=payMode;break;

        }
        return result;
    }

    public static String convertToString(Object object) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.writeValueAsString(object);
            } catch (Exception e) {
                return null;
            }
        }

    public static String convertToStringPretty(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            return null;
        }
    }

    public static String generateTraceId(){

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
            String dateTime = LocalDateTime.now().format(formatter);
            StringBuilder stringBuilder = new StringBuilder(dateTime);
            stringBuilder.append(ThreadLocalRandom.current().nextInt(50000, 800000));

            return String.format("SCH%s", stringBuilder.substring(6, 18));

    }

    public static Long getTimeTaken(Instant start) {
        return Duration.between(start, Instant.now()).toMillis();
    }


}
