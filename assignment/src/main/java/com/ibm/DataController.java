package com.ibm;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;
import java.io.*;
//Annotation to specify that this controller is REST controller
@RestController
public class DataController {
    private Boolean getCalled=false;
    private JSONArray jsonArray;
    @GetMapping(path = "json/corpusGet")
    public JSONArray GetData(){
       JSONParser parser = new JSONParser();
       jsonArray=null;
       try {

           Object obj = parser.parse(new FileReader(this.getClass().getClassLoader().getResource("Json/data.json").getFile()));
           jsonArray=(JSONArray)obj;
           System.out.println(jsonArray);
           getCalled=true;
       } catch (ParseException e) {
           e.printStackTrace();
       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       }
       return jsonArray;
   }

   @PostMapping(path = "json/corpusAdd")
    public JSONObject AddData (@RequestBody JSONObject object) throws MismatchedInputException, JsonEOFException,JsonParseException
   {
       // Checking that if GET is called before in order to make sure jsonArray has the complete data.
       if(getCalled==false)
       {
           GetData();
       }

       try (FileWriter file = new FileWriter(this.getClass().getClassLoader().getResource("Json/data.json").getFile())) {
           jsonArray.add(object);
           file.write(jsonArray.toJSONString());
           file.flush();

       } catch (IOException e) {
           e.printStackTrace();
       }

       System.out.print(object);
       return object;
   }

   @ExceptionHandler({MismatchedInputException.class, JsonParseException.class})
    public String handleException(){

        return "Correct Json Format is required as Input";
   }
   @ExceptionHandler({JsonEOFException.class})
    public String handleExceptionJson(){
        return "Json closing Bracket Required";
   }

}
