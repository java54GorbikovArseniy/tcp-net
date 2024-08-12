package telran.net;

import org.json.JSONObject;

import static telran.net.TcpConfigurationProperties.REQUEST_DATA_FIELD;
import static telran.net.TcpConfigurationProperties.REQUEST_TYPE_FIELD;

public interface Protocol {
    Response getResponse(Request request);

    default String getResponseWithJSON(String requestJSON) {
        JSONObject jsonObject = new JSONObject(requestJSON);
        String requestType = jsonObject.getString(REQUEST_TYPE_FIELD);
        String requestData = jsonObject.getString(REQUEST_DATA_FIELD);
        return getResponse(new Request(requestType, requestData)).toString();
    }
}
