


import java.io.File;

public class TschaFIOException extends TschaException{
     
     public static File file;
     
     public TschaFIOException(File f, String str){
          super(str);
          file = f;
     }
     
     public TschaFIOException(String str){
          super(str);
          file = null;
     }
     
     public TschaFIOException(File f){
          file = f;
     }
     
     public TschaFIOException(){
          file = null;
     }

}