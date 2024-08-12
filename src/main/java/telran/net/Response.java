package telran.net;

import org.json.JSONObject;
import static  telran.net.TcpConfigurationProperties.*;

public record Response (ResponseCode responseCode, String responseData){
    @Override
    public String toString(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(RESPONSE_CODE_FIELD, responseCode);
        jsonObject.put(RESPONSE_DATA_FIELD, responseData);
        return jsonObject.toString();
    }
}
