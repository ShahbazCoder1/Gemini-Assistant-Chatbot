/*Gemini ChatBot
Written By: Md Shahbaz Hashmi Ansari
Programing languages: Java
Code Version: V1.0
License ©: GPL-3.0 
Compatible with: BlueJ, Jvdroid, VS Code, etc...
 */

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.net.URI;
import java.io.IOException;
import java.util.Scanner;
public class ChatBot
{
    public String api_Key;
    public ChatBot(String api)
    {
        api_Key = api;
    }
    
    public static String setUp()
    {
        Scanner sc = new Scanner (System.in);
        System.out.println("Enter the API Key: ");
        String api_input = sc.nextLine();
        System.out.println("\nNote: To exit conservation click 'esc' button or type 's' and enter to exit.");
        System.out.println("\nGemini:");
        System.out.println("Namaste! \nHow can I help you today?");
        return api_input;
    }
    
    //Gemini API call
    public String api_Call(String text) throws IOException, InterruptedException
    {
        String jsonBody = prompt_Json(text); //prompt
        HttpRequest request = HttpRequest.newBuilder() //api call execuated here
            .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.0-pro:generateContent?key="+api_Key))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();

        // Send the request and get the response
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // Process the response
        if (response.statusCode() == 200) {
            return json_str(response.body());
        } else {
            return String.valueOf(response.statusCode());
        }

    }
    //to convert prompt to string
    public String prompt_Json(String s)
    {
        //System.out.println("{\"contents\":[{\"parts\":[{\"text\":\""+ s + "\"}]}]}");
        return "{\"contents\":[{\"parts\":[{\"text\":\""+ s + "\"}]}]}";
    }
    //to convert json to string
    public String json_str(String json)
    {
        int i = json.indexOf("\"text\": \"")+9;
        int j = json.indexOf("\"", i);
        if (i >= 0 && j >= 0) {
            return (json.substring(i, j)).replaceAll("[*\\\\]|\\\\n", "");
        } else {
            return json;
        }

    }

    public static void main(String[] args) throws IOException, InterruptedException 
    {
        Scanner sc = new Scanner (System.in);
        String api = setUp();
        ChatBot obj = new ChatBot (api);
        while(true) {
            System.out.println("\nEnter the Prompt: ");
            String prompt = sc.nextLine();
            if(prompt.equals("\u001B") || prompt.equals("s") || prompt.equals("S")) 
                break;
            System.out.println("\nGemini:\n"+ obj.api_Call(prompt));
        }
        System.out.println("Stop");
    }
}