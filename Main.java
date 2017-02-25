package memesGather;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

//FUCK THE COMMENTS!
public class Main {
	
    public static void main(String[] args) {
        System.out.println("Starting...");
    	
    	Main main = new Main();
    	main.go();
    }
    
    public void go(){
    	String firstSourcePath ="https://imgflip.com/?tgz=memes";//first url of meme page
    	ArrayList<ImgAndCaption> picUrls = new ArrayList<ImgAndCaption>();
    	int numberOfPages = 14;
    	
    	String path = firstSourcePath;
    	for (int i=0;i<numberOfPages;++i){
    		ScanPage(picUrls, path);
    		path = path + "&page="+Integer.toString(i+1);
    	}
    	

		System.out.println("Memes found: "+picUrls.size());
    	Output(picUrls);
    }
    
    private String CheckForErrorSymbols(String str){
    	str = str.replace("&#039;", "'");
    	str = str.replaceAll("&quot;", "\"");
    	return str;
    }
    
    private void ScanPage(ArrayList<ImgAndCaption> picUrls, String path){

    	for (String line : GetLinesWithPicUrl(path)){
    		ImgAndCaption ImgAC = new ImgAndCaption();

    		String t = ExtractPicUrl(line);
    		if (!t.equals("")){
    			ImgAC.SetUrl(t);
    		}
    		String ta = ExtractPicCaption(line);
    		if (!ta.equals("")){
    			ImgAC.SetCaption(ta);
    		}
    		if (!ImgAC.GetCaption().equals("") && !ImgAC.GetUrl().equals("")){
    			picUrls.add(ImgAC);
    		}
    	}
    }
    
    private ArrayList<String> GetLinesWithPicUrl(String pagePath){
    	ArrayList<String> lines = new ArrayList<String>();
    	
    	URL website;
    	try{
    		URLConnection connection = new URL(pagePath).openConnection();
        	connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        	connection.connect();

        	BufferedReader r  = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));

            String inputLine;
            while ((inputLine = r.readLine()) != null && lines.size()<=14){
                if (inputLine.contains("<a class='base-img-link'") ){
                    lines.add(inputLine);
                }
                //System.out.println(inputLine);
                //System.in.read();
            }
    	}
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    	return lines;
    }
    
    private String ExtractPicCaption(String line){
    	
    	String beginSymbols = "alt='";
    	String endSymbols = "|";
    	
    	System.out.println(line);
    	
    	int start = line.indexOf(beginSymbols)+beginSymbols.length();
    	start = line.indexOf("|", start);
    	++start;
    	int end = line.indexOf(endSymbols, start);
    	
    	System.out.print(start+" "+end+" \t");
    	
    	String all = "";
    	if (start!=-1 && end!=-1)
    		all = line.substring(start, end);
    	all = CheckForErrorSymbols(all);
    	System.out.println(all);
    	
    	return all;
    }
    
    private String ExtractPicUrl(String line){
    	String url = "";
    	
    	String beginSymbols = "src='//";
    	String endSymbols = "'";
    	
    	int start = line.indexOf(beginSymbols)+beginSymbols.length();
    	int end = line.indexOf(endSymbols, start);
    	
    	url = line.substring(start, end);
    	
    	return url;
    }

    private void Output(ArrayList<ImgAndCaption> list){
    	PrintWriter printWriter = null;
    	try {
		 	printWriter = new PrintWriter (new FileOutputStream( new File("urls.txt") ));
		} 
		catch (FileNotFoundException e) {
			System.err.println("Could not append or create file");
			e.printStackTrace();
		}

		for (ImgAndCaption i: list){
			printWriter.println(i.GetUrl()+" | "+i.GetCaption());
		}
		
		printWriter.close();
		System.out.println("Saved!");
    }
}
