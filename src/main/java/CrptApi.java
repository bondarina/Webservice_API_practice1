import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class CrptApi {

    private static final String API_URL = "https://ismp.crpt.ru/api/v3/lk/documents/commissioning/contract/create";

        private final int LIMIT;
       TimeUnit timeUnit = TimeUnit.SECONDS;
      long seconds = timeUnit.toSeconds(5);
        private final AtomicInteger COUNTER = new AtomicInteger(0);

        public CrptApi (TimeUnit timeUnit, int requestLimit) {
            this.timeUnit=timeUnit;
            this.LIMIT=requestLimit;
        }

       public synchronized boolean isRequestAllowed() {
            if (COUNTER.get() <= LIMIT) {
                COUNTER.incrementAndGet();
                return true;
            } else {
                return false;
            }
       }

       public synchronized void releaseRequest() {
            COUNTER.decrementAndGet();
       }

    public String CommissioningRfCreate(MyObject document, String signature) throws IOException, URISyntaxException, UnsupportedCharsetException {
        HttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(new URI(API_URL));

        StringEntity requestEntity = new StringEntity(document.toString(), signature);
        httpPost.setEntity(requestEntity);

        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", " Bearer eyJhbGciOiJIUzI1NiIsInR5cC....T7QquJwtJxiFxDxpYitE7lcNebiDWe9MQOTa6E62zjs");

        HttpResponse httpResponse = httpClient.execute(httpPost);

        HttpEntity httpEntity = httpResponse.getEntity();
        String responseBody = EntityUtils.toString(httpEntity);

       int statusCode = httpResponse.getStatusLine().getStatusCode();
       if (statusCode != 200) {
           throw new IOException("Unexpected status code: " + statusCode);
       }

       return responseBody;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        String json = readFile("data.json");

        ObjectMapper mapper = new ObjectMapper();
        MyObject myObject = mapper.readValue(json, MyObject.class);

      // System.out.println(json);

        CrptApi crpt = new CrptApi(TimeUnit.SECONDS, 5);
        crpt.CommissioningRfCreate(myObject, "bfad0002-9498-434b-afa2-5927fc1f6837");


    }

    private static String readFile(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(
                MyObject.class.getResourceAsStream(filePath)));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    static class MyObject {
        private String doc_id;
        private String doc_status;
        private String doc_type;
        private boolean importRequest;
        private String owner_inn;
        private String participant_inn;
        private String producer_inn;
        private String production_date;
        private String production_type;
        private String reg_date;
        private String reg_number;

        public String getDoc_id() {
            return doc_id;
        }

        public void setDoc_id(String doc_id) {
            this.doc_id = doc_id;
        }

        public String getDoc_status() {
            return doc_status;
        }

        public void setDoc_status(String doc_status) {
            this.doc_status = doc_status;
        }

        public String getDoc_type() {
            return doc_type;
        }

        public void setDoc_type(String doc_type) {
            this.doc_type = doc_type;
        }

        public boolean isImportRequest() {
            return importRequest;
        }

        public void setImportRequest(boolean importRequest) {
            this.importRequest = importRequest;
        }

        public String getOwner_inn() {
            return owner_inn;
        }

        public void setOwner_inn(String owner_inn) {
            this.owner_inn = owner_inn;
        }

        public String getParticipant_inn() {
            return participant_inn;
        }

        public void setParticipant_inn(String participant_inn) {
            this.participant_inn = participant_inn;
        }

        public String getProducer_inn() {
            return producer_inn;
        }

        public void setProducer_inn(String producer_inn) {
            this.producer_inn = producer_inn;
        }

        public String getProduction_date() {
            return production_date;
        }

        public void setProduction_date(String production_date) {
            this.production_date = production_date;
        }

        public String getProduction_type() {
            return production_type;
        }

        public void setProduction_type(String production_type) {
            this.production_type = production_type;
        }

        public String getReg_date() {
            return reg_date;
        }

        public void setReg_date(String reg_date) {
            this.reg_date = reg_date;
        }

        public String getReg_number() {
            return reg_number;
        }

        public void setReg_number(String reg_number) {
            this.reg_number = reg_number;
        }
    }
}
