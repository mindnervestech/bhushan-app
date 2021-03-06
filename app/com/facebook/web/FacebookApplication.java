/**
 * 
 */
package com.facebook.web;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import play.Play;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;

import com.facebook.web.vo.FacebookPostVO;


/**
 * @author Vinod Porwal.
 *
 */
public class FacebookApplication extends Controller {

	static final String BASE_URL = Play.application().configuration().getString("baseUrl");
	static final String FILE_PATH = Play.application().configuration().getString("filePath");

	public static Result index() {
		String prod_url = "http://178.79.182.229:7070";
		String local_url = "http://localhost:9000";
		return ok(com.facebook.web.view.html.index.render("Facebook Page Posts",prod_url));
	}

	public static Result downloadContents() throws IOException {

		DynamicForm form = DynamicForm.form().bindFromRequest();
		String accessToken = form.get("accessToken");
        System.out.println("Access Token: "+accessToken);
		String pagelist = form.get("spages");
        List<String> list = new ArrayList<String>(Arrays.asList(pagelist.split(",")));
        int listSize = list.size();
        List<String> pages = new ArrayList<String>();
    	pages = list.subList(0, listSize - 2);
    	System.out.println("Selected Pages:");
       	for(String page:pages){System.out.println(page);}
       	String fromDate = list.get(list.size() - 2);
        System.out.println("From Date: "+fromDate);
        String toDate = list.get(list.size() - 1);
        System.out.println("To Date: "+toDate);
      
        DateFormat dtype = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
        DateFormat dfr = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dtf = new SimpleDateFormat("hh:mm:ss");
        Long f=null, t=null;//"yyyy-MM-dd'T'HH:mm:ssZ"
        DateFormat df = new SimpleDateFormat("EEE MMM dd yyyy"); 
        Date startDate, endDate;
        try {if(fromDate.equals("notselected")){
        	startDate = null;
        	//f=Long.parseLong(fromDate);
          }else{
        	startDate = df.parse(fromDate);
          	Calendar c = Calendar.getInstance();
          	c.setTime(startDate);
          	c.set(Calendar.HOUR, 0);
          	c.set(Calendar.MINUTE,0);
          	c.set(Calendar.SECOND, 0);
          	c.set(Calendar.MILLISECOND, 0);
          	startDate = c.getTime();
          	f = startDate.getTime()/1000;
        	  //startDate = df.parse(fromDate);
              //f = (startDate.getTime())/1000;
            }
        
        } catch (ParseException e) {
            e.printStackTrace();
            startDate = null;
        }
        try {
        if(toDate.equals("notselected")){
        	endDate = null;
           }else{ 
        	   endDate = df.parse(toDate);
             	Calendar c = Calendar.getInstance();
             	c.setTime(endDate);
             	c.set(Calendar.HOUR, 23);
             	c.set(Calendar.MINUTE,59);
             	c.set(Calendar.SECOND, 59);
             	c.set(Calendar.MILLISECOND, 999);
             	endDate = c.getTime();
             	t = endDate.getTime()/1000;
             	//endDate = df.parse(toDate);
           //t = (endDate.getTime())/1000;
            	
            }
        } catch (ParseException e) {
            e.printStackTrace();
            endDate = null;
        }  
         
        System.out.println("from: "+f+" to: "+t);
        
       	//	Properties props = new Properties();

	//	props.put("BR31India", "BR31India");
		//props.put("BaseballHeroes", "BaseballHeroes");

		//Set<Object> keys = props.keySet();

		final List<FacebookPostVO> postLists = new ArrayList<FacebookPostVO>();
		//for (Object key: keys) {
		//	String pageName = props.getProperty((String)key);
		for (String pageName: pages) {	
			try {
				JSONObject jsnobject;
				if(startDate != null && endDate != null){
				jsnobject = 
						getJsonObjFromUrl(BASE_URL + pageName + "/posts?method=GET&format=json&suppress_http_code=1&limit=50&access_token=" + accessToken+"&since="+f+"&until="+t);//new JSONObject(postStr);
		//		System.out.println(BASE_URL + pageName + "/posts?method=GET&format=json&suppress_http_code=1&access_token=" + accessToken+"&since="+fDate+"&until="+tDate);
				}
				else if( startDate != null && endDate == null){
					jsnobject = 
							getJsonObjFromUrl(BASE_URL + pageName + "/posts?method=GET&format=json&suppress_http_code=1&limit=50&access_token=" + accessToken+"&since="+f);	
				}else if( startDate == null && endDate != null){
					jsnobject = 
							getJsonObjFromUrl(BASE_URL + pageName + "/posts?method=GET&format=json&suppress_http_code=1&limit=50&access_token=" + accessToken+"&until="+t);
				}else{
					jsnobject = 
							getJsonObjFromUrl(BASE_URL + pageName + "/posts?method=GET&format=json&suppress_http_code=1&limit=50&access_token=" + accessToken);
				}
				JSONArray jsonArray = jsnobject.getJSONArray("data");

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject explrObject = jsonArray.getJSONObject(i);
					String type = explrObject.getString("type");
					if ("photo".equalsIgnoreCase(type)) {
						JSONObject obj = explrObject.getJSONObject("from");

						FacebookPostVO vo = new FacebookPostVO();
						
						vo.setFrom(obj.getString("name"));
						
						if (explrObject.has("message")) { 
							vo.setMessage(explrObject.getString("message")); 
						} else { 
							vo.setMessage(""); 
						}
						
						if (explrObject.has("picture")) { 
							vo.setPicture(explrObject.getString("picture")); 
						} else { 
							vo.setPicture(""); 
						}
						
						if (explrObject.has("link")) { 
							vo.setLink(explrObject.getString("link")); 
						} else { 
							vo.setLink(""); 
						}
						
						JSONObject picture = getJsonObjFromUrl(
								BASE_URL + explrObject.getString("object_id") + "/picture?type=normal&method=GET&format=json&redirect=false&access_token=" + accessToken);//new JSONObject(postStr);
						
						JSONObject data = picture.getJSONObject("data");
						vo.setPicture(data.getString("url"));
						
						vo.setType(type);
						
						if (explrObject.has("icon")) { 
							vo.setIcon(explrObject.getString("icon")); 
						} else { 
							vo.setIcon(""); 
						}
							
						if (explrObject.has("created_time")) {
							Date date = dtype.parse(explrObject.getString("created_time"));
							Date time = dtype.parse(explrObject.getString("created_time"));
							vo.setCreated_time(dtf.format(time));
							vo.setCreated_Date(dfr.format(date));
						} else { 
							vo.setCreated_time("");
							vo.setCreated_Date("");
						}
						
						
						if (explrObject.has("updated_time")) { 
							Date date = dtype.parse(explrObject.getString("updated_time"));
							Date time = dtype.parse(explrObject.getString("updated_time"));
							vo.setUpdated_time(dtf.format(time));
							vo.setUpdated_Date(dfr.format(date));
						} else { 
							vo.setUpdated_time("");
							vo.setUpdated_Date("");
						}
						
						postLists.add(vo);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				
			}
			System.out.println("Done for page: "+pageName);
		}

		//final String dateName = generateCsv(postLists);
		
		
		CellProcessor[] processors = new CellProcessor[] {
				new NotNull(), // name
				new NotNull(), // message
				new NotNull(), // picture
				new NotNull(), // link
				new NotNull(), // icon
				new NotNull(), // created_Date
				new NotNull(), // created_time
				new NotNull(), // updated_Date
				new NotNull() // updated_time
		};

		ICsvBeanWriter beanWriter = null;
		File file = new File(FILE_PATH);
		if(!file.exists()){
			file.mkdir();
		}
		long currentTime = System.currentTimeMillis();
		Date date = new Date(currentTime);
		DateFormat df1 = new SimpleDateFormat("yyyyMMdd-HHmm");
		String dateName = df1.format(date);

		beanWriter = new CsvBeanWriter(new FileWriter(FILE_PATH + File.separator + "facebook_" + dateName + ".csv"),
				CsvPreference.STANDARD_PREFERENCE);
		String[] header = {"from","message","picture","link","icon","created_Date","created_time","updated_Date","updated_time"};
		beanWriter.writeHeader(header);
		for (FacebookPostVO result : postLists) {
			beanWriter.write(result, header, processors);
		}
		if(beanWriter != null){
			beanWriter.flush();
			beanWriter.close();
		}
		
		response().setContentType("application/octet-stream");
		response().setHeader("Content-Disposition",
				"attachment;filename=facebook_" + dateName +".csv");
		response().setCookie("fileDownload", "true");
		return ok(new File(FILE_PATH + File.separator + "facebook_" + dateName + ".csv"));
		
		//return ok("Response");
		 }
	
	private static JSONObject getJsonObjFromUrl(String urlString)
			throws MalformedURLException, URISyntaxException, IOException,
			ClientProtocolException, JSONException {
			URL url = new URL(urlString);
			URI uri = url.toURI();
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(uri);
			HttpResponse response = httpclient.execute(httpGet);
			String jsonString = EntityUtils.toString(response.getEntity()).toString();
			
			JSONObject jsonObj = new JSONObject(jsonString);
			return jsonObj;
	}
	
	private static String generateCsv(final List<FacebookPostVO> postLists) throws IOException {
		CellProcessor[] processors = new CellProcessor[] {
				new NotNull(), // name
				new NotNull(), // message
				new NotNull(), // picture
				new NotNull(), // link
				new NotNull(), // icon
				new NotNull(), // created_Date
				new NotNull(), // created_time
				new NotNull(), // updated_Date
				new NotNull() // updated_time
		};

		ICsvBeanWriter beanWriter = null;
		File file = new File(FILE_PATH);
		if(!file.exists()){
			file.mkdir();
		}
		long currentTime = System.currentTimeMillis();
		Date date = new Date(currentTime);
		DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmm");
		String dateName = df.format(date);

		beanWriter = new CsvBeanWriter(new FileWriter(FILE_PATH + File.separator + "facebook_" + dateName + ".csv"),
				CsvPreference.STANDARD_PREFERENCE);
		String[] header = {"from","message","picture","link","icon","created_Date","created_time","updated_Date","updated_time"};
		beanWriter.writeHeader(header);
		for (FacebookPostVO result : postLists) {
			beanWriter.write(result, header, processors);
		}
		if(beanWriter != null){
			beanWriter.flush();
			beanWriter.close();
		}
		
		return dateName;
	}

}
