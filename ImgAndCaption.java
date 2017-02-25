package memesGather;

public class ImgAndCaption{
    private String url = "";
    private String captions= "";
    
    public String GetUrl(){
        return url;
    }
    public void SetUrl(String u){
        url = u;
    }
    
    public String GetCaption(){
    	return captions;
    }
    
    public void SetCaption(String c){
    	captions = c;
    }
    
}