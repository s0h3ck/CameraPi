package libcom;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * Sert a poster une image a une url 
 * avec des metadonnees en JSON
 * 
 * @author Antoine Morin-Paulhus
 */
public class ImagePoster {
	private File file = null;
	
	public ImagePoster(File img_file) {
		file = img_file;
	}
	
	public void post(String url, String JSON) throws ConnectException, URISyntaxException{
		HttpClient httpClient = new DefaultHttpClient();
	    HttpContext localContext = new BasicHttpContext();
	    URI uri = new URI(url);
	    HttpPost httpPost = new HttpPost(uri);
	    
	    try {
	        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
	        entity.addPart("uploadFile", new FileBody(file));
	        entity.addPart("jsonData", new StringBody(JSON));
	        httpPost.setEntity(entity);

	        try{
	        	HttpResponse response = httpClient.execute(httpPost, localContext);
	        } catch (ConnectException e){
	        	// Do nothing, exception is normal
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
