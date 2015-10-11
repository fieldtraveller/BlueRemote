package file_operations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
//import android.os.Environment;

public class file_operations
{
	public static <T> void save_to_file(Context context,String fileName,T object_to_save) 
	{
	    try 
	    {
//	    	File file = new File(Environment.getExternalStorageDirectory(), fileName);
//	    	file.createNewFile();
//	        FileOutputStream file_output_stream = new FileOutputStream(file);
	        
	        FileOutputStream file_output_stream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
	        
	        ObjectOutputStream object_output_stream = new ObjectOutputStream(file_output_stream);
	        object_output_stream.writeObject(object_to_save);
	        
	        object_output_stream.close();
	        file_output_stream.close();
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	}

	@SuppressWarnings("unchecked")
	public static <T> T read_from_file(Context context,String fileName) 
	{
		T object_read=null;
	    
	    try 
	    {
//	    	FileInputStream file_input_stream = new FileInputStream(Environment.getExternalStorageDirectory().getPath()+"/"+fileName);
	    	FileInputStream file_input_stream = context.openFileInput(fileName);
	    			
	    	ObjectInputStream object_input_stream = new ObjectInputStream(file_input_stream);
	        object_read = (T)object_input_stream.readObject();
	        
	        object_input_stream.close();
	        file_input_stream.close();
	    }
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	    catch(ClassNotFoundException e) 
	    {
	        e.printStackTrace();
	    }
	    
	    return object_read;
	}
	
	public static boolean file_exists(Context context, String filename) 
	{    
	    File file = context.getFileStreamPath(filename);
	    
	    if(file == null || !file.exists()) 
	    {
	        return false;
	    }
	    
	    return true;
	}
}
